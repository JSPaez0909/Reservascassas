package com.cassa.reservas.security;

import com.cassa.reservas.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔥 [L1] EXCLUIR ENDPOINTS PÚBLICOS (MEJORADO)
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 [L2] USAR CONSTANTE HttpHeaders (MEJOR PRÁCTICA)
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 🔥 [L3] VALIDACIÓN ROBUSTA DEL HEADER
        if (!isValidBearerToken(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 🔥 [L4] EXTRAER TOKEN LIMPIO
            String token = extractToken(authHeader);

            // 🔥 [L5] VALIDAR TOKEN (NUEVO)
            if (!jwtService.isTokenExpired(token)) {

                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                // 🔥 [L6] VALIDACIÓN MÁS SEGURA
                if (isValidAuthentication(username, role)) {

                    // 🔥 [L7] ROLE
                    String formattedRole = role.startsWith("ROLE_")
                            ? role
                            : "ROLE_" + role.toUpperCase();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(formattedRole))
                            );

                    // 🔥 [L8] SETEAR DETALLES (IMPORTANTE PARA AUDITORÍA)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 🔥 [L9] SETEAR CONTEXTO DE SEGURIDAD
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            // 🔥 [L10] LIMPIEZA SEGURA
            SecurityContextHolder.clearContext();

            // 🔥 OPCIONAL (DEBUG)
            System.out.println("JWT ERROR: " + e.getMessage());
        }

        // 🔥 [L11] CONTINUAR FLUJO SIEMPRE
        filterChain.doFilter(request, response);
    }

    // 🔥 MÉTODOS AUXILIARES

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources");
    }

    private boolean isValidBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isValidAuthentication(String username, String role) {
        return username != null &&
                role != null &&
                SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
package com.cassa.reservas.security;

import com.cassa.reservas.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //1. EXCLUIR ENDPOINTS PÚBLICOS
        String path = request.getServletPath();
        if (path.startsWith("/auth") ||
                path.startsWith("/swagger") ||
                path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. OBTENER HEADER
        final String authHeader = request.getHeader("Authorization");

        // 3. VALIDAR HEADER
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //4. EXTRAER TOKEN
            String token = authHeader.substring(7);

            //5. EXTRAER USUARIO
            String username = jwtService.extractUsername(token);

            //6. VALIDAR SI YA ESTÁ AUTENTICADO
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                //7. CARGAR USUARIO
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //8. VALIDAR TOKEN
                if (jwtService.isTokenValid(token, userDetails)) {

                    //9. CREAR AUTENTICACIÓN
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    //10. AGREGAR DETALLES (IP, sesión, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    //11. SETEAR CONTEXTO
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            //12. LIMPIAR CONTEXTO SI TOKEN ES INVÁLIDO
            SecurityContextHolder.clearContext();
        }

        //13. CONTINUAR FILTRO
        filterChain.doFilter(request, response);
    }
}
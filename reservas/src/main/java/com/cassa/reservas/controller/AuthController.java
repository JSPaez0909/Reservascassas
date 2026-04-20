package com.cassa.reservas.controller;

import com.cassa.reservas.dto.AuthRequest;
import com.cassa.reservas.dto.AuthResponse;
import com.cassa.reservas.model.Role;
import com.cassa.reservas.model.User;
import com.cassa.reservas.service.JwtService;
import com.cassa.reservas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 🔥 [CAMBIO 1] REGISTRO CONTROLADO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // 🔥 [CAMBIO 2] ASIGNAR ROLE POR DEFECTO
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        User savedUser = userService.register(user);

        return ResponseEntity.ok(savedUser);
    }

    // 🔥 [CAMBIO 3] LOGIN ROBUSTO
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // 🔥 [CAMBIO 4] AUTENTICACIÓN
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 🔥 [CAMBIO 5] OBTENER USUARIO COMPLETO
        User user = (User) authentication.getPrincipal();

        // 🔥 [CAMBIO 6] GENERAR TOKEN CON ROLE
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
package com.cassa.reservas.controller;

import com.cassa.reservas.dto.ReservaRequest;
import com.cassa.reservas.dto.ReservaResponse;
import com.cassa.reservas.model.Reserva;
import com.cassa.reservas.model.User;
import com.cassa.reservas.repository.ReservaRepository;
import com.cassa.reservas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    // 🔹 Inyección de dependencias
    private final UserRepository userRepository;
    private final ReservaRepository reservaRepository;

    // =========================================================
    // ✅ CREAR RESERVA (SOLO USER)
    // =========================================================
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ReservaResponse crear(@RequestBody ReservaRequest request, Authentication authentication) {

        // 🔹 1. Obtener usuario desde el token (JWT)
        String username = authentication.getName();

        // 🔹 2. Buscar usuario en base de datos
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔹 3. Mapear DTO → Entidad
        Reserva reserva = new Reserva();
        reserva.setFecha(request.getFecha());
        reserva.setDescripcion(request.getDescripcion());
        reserva.setUser(user); // 🔥 clave: relación obligatoria

        // 🔹 4. Guardar en base de datos
        Reserva saved = reservaRepository.save(reserva);

        // 🔹 5. Mapear Entidad → Response DTO
        return ReservaResponse.builder()
                .id(saved.getId())
                .fecha(saved.getFecha())
                .descripcion(saved.getDescripcion())
                .usuarioEmail(saved.getUser().getEmail())
                .build();
    }

    // =========================================================
    // ✅ LISTAR RESERVAS (USER Y ADMIN)
    // =========================================================
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public List<ReservaResponse> listar() {

        // 🔹 Convertir lista de entidades a DTO
        return reservaRepository.findAll()
                .stream()
                .map(reserva -> ReservaResponse.builder()
                        .id(reserva.getId())
                        .fecha(reserva.getFecha())
                        .descripcion(reserva.getDescripcion())
                        .usuarioEmail(reserva.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());
    }
}

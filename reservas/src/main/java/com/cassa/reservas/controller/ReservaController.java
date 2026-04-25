package com.cassa.reservas.controller;

import com.cassa.reservas.dto.ReservaRequest;
import com.cassa.reservas.dto.ReservaResponse;
import com.cassa.reservas.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    //  SOLO inyectamos el Service (NO repositories)
    private final ReservaService reservaService;

    // =========================================================
    // CREAR RESERVA (SOLO USER)
    // =========================================================
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReservaResponse> crear(
            @Valid @RequestBody ReservaRequest request,
            Authentication authentication) {

        //  solo obtenemos el email
        String email = authentication.getName();

        //  delegamos lógica al service
        ReservaResponse response = reservaService.crearReserva(request, email);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // LISTAR MIS RESERVAS (USER)
    // =========================================================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResponse>> misReservas(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(reservaService.obtenerMisReservas(email));
    }

    // =========================================================
    // LISTAR TODAS (ADMIN)
    // =========================================================
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarTodas() {

        return ResponseEntity.ok(reservaService.obtenerTodas());
    }
}

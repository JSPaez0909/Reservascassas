package com.cassa.reservas.service;

import com.cassa.reservas.dto.ReservaRequest;
import com.cassa.reservas.dto.ReservaResponse;
import com.cassa.reservas.model.Reserva;
import com.cassa.reservas.model.User;
import com.cassa.reservas.repository.ReservaRepository;
import com.cassa.reservas.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UserRepository userRepository;

    public ReservaService(ReservaRepository reservaRepository, UserRepository userRepository) {
        this.reservaRepository = reservaRepository;
        this.userRepository = userRepository;
    }

    //  CREAR RESERVA
    @Transactional
    public ReservaResponse crearReserva(ReservaRequest request, String email) {

        //  Buscar usuario
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //  Crear reserva (AJUSTADO A TU MODELO)
        Reserva reserva = new Reserva();
        reserva.setFecha(request.getFecha());
        reserva.setDescripcion(request.getDescripcion());
        reserva.setUser(user);

        Reserva guardada = reservaRepository.save(reserva);

        //  Mapear respuesta
        return mapToResponse(guardada);
    }

    //  MIS RESERVAS (AJUSTADO)
    public List<ReservaResponse> obtenerMisReservas(String email) {

        return reservaRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //  TODAS (ADMIN)
    public List<ReservaResponse> obtenerTodas() {

        return reservaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //  MÉTODO PRIVADO PARA MAPEO (MUY IMPORTANTE)
    private ReservaResponse mapToResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .fecha(reserva.getFecha())
                .descripcion(reserva.getDescripcion())
                .usuarioEmail(reserva.getUser().getEmail())
                .build();
    }
}
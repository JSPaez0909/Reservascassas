package com.cassa.reservas.repository;

import com.cassa.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByFechaAndDescripcion(LocalDate fecha, String descripcion);
    List<Reserva> findByUserEmail(String email);
}

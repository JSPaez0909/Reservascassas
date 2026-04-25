package com.cassa.reservas.model;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FECHA DE LA RESERVA
    @Column(nullable = false)
    private LocalDate fecha;

    // ✅ DESCRIPCIÓN
    @Column(nullable = false)
    private String descripcion;

    // ✅ AUDITORÍA (NUEVO)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ RELACIÓN CON USUARIO (ORDEN CORREGIDO)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // ✅ SET AUTOMÁTICO DE FECHA DE CREACIÓN
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

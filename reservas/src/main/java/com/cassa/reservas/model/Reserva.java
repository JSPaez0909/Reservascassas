package com.cassa.reservas.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;


@Entity
@Table(name = "reservas")
@Data


public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String descripcion;

    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)

    // Relación con usuario
    @ManyToOne
    private User user;

}

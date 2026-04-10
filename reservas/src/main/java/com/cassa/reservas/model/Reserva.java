package com.cassa.reservas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fecha;
    private String descripcion;
    private String usuarioEmail;
}

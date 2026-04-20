package com.cassa.reservas.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ReservaResponse {

    private Long id;
    private LocalDate fecha;
    private String descripcion;
    private String usuarioEmail;
}
package com.cassa.reservas.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaRequest {

    private LocalDate fecha;
    private String descripcion;
}
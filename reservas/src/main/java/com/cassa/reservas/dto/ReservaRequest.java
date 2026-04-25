package com.cassa.reservas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;

@Data
public class ReservaRequest {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotNull(message = "La descripción es obligatoria")
    private String descripcion;
}

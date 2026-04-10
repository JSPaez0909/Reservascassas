package com.cassa.reservas.controller;

import com.cassa.reservas.model.Reserva;
import com.cassa.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaRepository reservaRepository;

    @PostMapping
    public Reserva crear(@RequestBody Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @GetMapping
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }
}

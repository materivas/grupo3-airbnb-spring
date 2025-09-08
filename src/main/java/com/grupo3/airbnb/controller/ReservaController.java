/*
package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.service.ReservaService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @GetMapping("/{usuario}")
    public ResponseEntity<java.util.List<ReservaDTO>> getAllReservas(@PathVariable("usuario") String usuario) {
        try {
            java.util.List<ReservaDTO> reservas = reservaService.getAllReservas()
                    .stream()
                    .filter( reservaDTO -> reservaDTO.getHuesped().equalsIgnoreCase(usuario))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
*/
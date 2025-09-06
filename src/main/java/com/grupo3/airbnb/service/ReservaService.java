package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.repository.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private PropiedadService propiedadService;

    public Reserva createReserva(int nroHuespedes, LocalDate fechaInicio, LocalDate fechaFin, String propiedadId, String usuarioId ) {
        Reserva reserva = new Reserva();
        try {
            Propiedad p= propiedadService.getPropiedad(Long.valueOf(propiedadId));
            double precioTotal = p.getPrecioPorNoche() * nroHuespedes * (fechaFin.toEpochDay() - fechaInicio.toEpochDay());
            reserva.setPropiedad(p);
            reserva.setPrecioTotal(precioTotal);
          } catch ( Exception e) {
                throw new RuntimeException("Propiedad no encontrada");
            }
        reserva.setHuesped( usuarioId);
        reserva.setNroHuespedes(nroHuespedes);
        reserva.setEntrada(java.sql.Timestamp.valueOf(fechaInicio.atStartOfDay()));
        reserva.setSalida(java.sql.Timestamp.valueOf(fechaFin.atStartOfDay()));
        return reservaRepository.save(reserva);
    }

    public void deleteReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    public List<ReservaDTO> getAllReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(this::convertToReservaDTO)
                .toList();
    }

    public Reserva getReserva(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    public Reserva getReserva( String usuarioId) {
        return reservaRepository.findByHuesped(usuarioId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    //metodos de conversion
    public ReservaDTO convertToReservaDTO(Reserva reserva) {
        return new ReservaDTO(
                reserva.getPropiedad().getTitulo(),
                reserva.getNroHuespedes(),
                reserva.getHuesped(),
                reserva.getEntrada(),
                reserva.getSalida(),
                reserva.getPrecioTotal()
        );
    }
}

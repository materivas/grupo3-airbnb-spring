package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.repository.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private PropiedadService propiedadService;

    public Reserva createReserva(int nroHuespedes, LocalDate fechaInicio, LocalDate fechaFin, String propiedadId, String usuarioId) {
        Reserva reserva = new Reserva();
        try {
            Propiedad p = propiedadService.getPropiedad(Long.valueOf(propiedadId));
            double precioTotal = p.getPrecioPorNoche() * nroHuespedes * (fechaFin.toEpochDay() - fechaInicio.toEpochDay());
            reserva.setPropiedad(p);
            reserva.setPrecioTotal(precioTotal);
        } catch (Exception e) {
            throw new RuntimeException("Propiedad no encontrada");
        }
        reserva.setHuesped(usuarioId);
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

    public Reserva getReserva(String usuarioId) {
        return reservaRepository.findByHuesped(usuarioId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    //Metodo que agregué para obtener la lista de reservas del usuario
    public List<ReservaDTO> getReservasByUsuario(String usuarioId) {
        return reservaRepository.findByHuespedIgnoreCase(usuarioId)
                .stream()
                .map(this::convertToReservaDTO)
                .toList();
    }

    // Obtener todos los usuarios que tienen reservas
    public List<String> getUsuariosConReservas() {
        return reservaRepository.findAll().stream()
                .map(Reserva::getHuesped)
                .distinct()
                .collect(Collectors.toList());
    }
    
    //metodos de conversion
    private ReservaDTO convertToReservaDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setPropiedadTitulo(reserva.getPropiedad().getTitulo());
        dto.setNroHuespedes(reserva.getNroHuespedes());
        dto.setHuesped(reserva.getHuesped());
        dto.setEntrada(reserva.getEntrada().toLocalDateTime());
        dto.setSalida(reserva.getSalida().toLocalDateTime());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setDiasEstadia(calcularDiasEstadia(reserva));

        // Obtiene primera imagen de la propiedad
        if (!reserva.getPropiedad().getImages().isEmpty()) {
            dto.setImagenUrl(reserva.getPropiedad().getImages().get(0).getUrl());
        } else {
            dto.setImagenUrl("/images/placeholder.jpg");
        }

        return dto;
    }

    public int calcularDiasEstadia(Reserva reserva) {
        long dias = reserva.getSalida().toLocalDateTime().toLocalDate().toEpochDay() - reserva.getEntrada().toLocalDateTime().toLocalDate().toEpochDay();
        return (int) dias;
    }
}

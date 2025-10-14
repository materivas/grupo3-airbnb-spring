package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.repository.IReservaRepository;
import com.grupo3.airbnb.repository.IReviewRepository;

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
    private IReviewRepository reviewRepository;
    
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

    // Obtener reservas sin reseñar del usuario
    public List<ReservaDTO> getReservasSinResenar(String usuarioId) {
        return reservaRepository.findByHuespedIgnoreCase(usuarioId)
                .stream()
                .map(this::convertToReservaDTO)
                .filter(ReservaDTO::isPuedeCalificar) // Solo las que pueden calificar (checkout pasado y sin review)
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
        dto.setId(reserva.getId()); // ojo, lo vas a necesitar para el botón
        dto.setPropiedadTitulo(reserva.getPropiedad().getTitulo());
        dto.setNroHuespedes(reserva.getNroHuespedes());
        dto.setHuesped(reserva.getHuesped());
        dto.setEntrada(reserva.getEntrada().toLocalDateTime());
        dto.setSalida(reserva.getSalida().toLocalDateTime());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setDiasEstadia(calcularDiasEstadia(reserva));

        // Imagen
        if (!reserva.getPropiedad().getImages().isEmpty()) {
            dto.setImagenUrl(reserva.getPropiedad().getImages().get(0).getUrl());
        } else {
            dto.setImagenUrl("/images/placeholder.jpg");
        }

        // Validar si ya pasó el checkout
        LocalDate fechaCheckout = reserva.getSalida().toLocalDateTime().toLocalDate();
        boolean checkoutPasado = fechaCheckout.isBefore(LocalDate.now());

        // Validar si ya tiene review
        boolean sinReview = reviewRepository.findByReservaIdAndUsuario(reserva.getId(), reserva.getHuesped())
                                            .isEmpty();

        // Puede calificar si checkout pasó y no existe review previa
        dto.setPuedeCalificar(checkoutPasado && sinReview);

        return dto;
    }


    public int calcularDiasEstadia(Reserva reserva) {
        long dias = reserva.getSalida().toLocalDateTime().toLocalDate().toEpochDay() - reserva.getEntrada().toLocalDateTime().toLocalDate().toEpochDay();
        return (int) dias;
    }

    // Obtener reservas de las propiedades del anfitrión (para Historia 4: comentar sobre huésped)
    public List<ReservaDTO> getReservasByAnfitrion(Long anfitriónDni) {
        List<Propiedad> propiedades = propiedadService.getPropiedadesByAnfitrionEntity(anfitriónDni);

        return reservaRepository.findAll().stream()
                .filter(reserva -> propiedades.contains(reserva.getPropiedad()))
                .filter(reserva -> {
                    // Solo mostrar reservas completadas (checkout en el pasado)
                    LocalDate fechaCheckout = reserva.getSalida().toLocalDateTime().toLocalDate();
                    return fechaCheckout.isBefore(LocalDate.now());
                })
                .map(this::convertToReservaDTOParaAnfitrion)
                .toList();
    }

    private ReservaDTO convertToReservaDTOParaAnfitrion(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setPropiedadId(reserva.getPropiedad().getId());
        dto.setPropiedadTitulo(reserva.getPropiedad().getTitulo());
        dto.setNroHuespedes(reserva.getNroHuespedes());
        dto.setHuesped(reserva.getHuesped());
        dto.setEntrada(reserva.getEntrada().toLocalDateTime());
        dto.setSalida(reserva.getSalida().toLocalDateTime());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setDiasEstadia(calcularDiasEstadia(reserva));

        if (!reserva.getPropiedad().getImages().isEmpty()) {
            dto.setImagenUrl(reserva.getPropiedad().getImages().get(0).getUrl());
        } else {
            dto.setImagenUrl("/images/placeholder.jpg");
        }

        return dto;
    }
}

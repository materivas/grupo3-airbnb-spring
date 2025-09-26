package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import com.grupo3.airbnb.repository.IReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;
    @Autowired
    private IReservaRepository reservaRepository;

    // Método nuevo
    public Propiedad getPropiedad(Long id) {
        return propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    public List<PropiedadListDTO> getAllPropiedades(Double precioMin, Double precioMax, String moneda, LocalDate fechaMin, LocalDate fechaMax) {

		List<Propiedad> propiedades = propiedadRepository.findAll();
		
		return propiedades.stream().filter(propiedad -> {
			
			// --- Filtro por precio mínimo ---
			if (precioMin != null && propiedad.getPrecioPorNoche() < precioMin) {
				return false;
			}
			
			// --- Filtro por precio máximo ---
			if (precioMax != null && propiedad.getPrecioPorNoche() > precioMax) {
				return false;
			}
			
			// --- Filtro por moneda ---
			if (moneda != null && !moneda.isEmpty() && !moneda.equals(propiedad.getMoneda())) {
				return false;
			}
			
			// --- Filtro por disponibilidad ---
			if (fechaMin != null || fechaMax != null) {
				LocalDate start = fechaMin != null ? fechaMin : fechaMax;
				LocalDate end = fechaMax != null ? fechaMax : fechaMin;
				
				if (!isPropiedadDisponible(propiedad.getId(), start, end)) {
					return false;
				}
			}
			
			return true; // Pasa todos los filtros
		})
		.map(this::convertToDTO)
		.collect(Collectors.toList());
	}


    private PropiedadListDTO convertToDTO(Propiedad propiedad) {
        return new PropiedadListDTO(
                propiedad.getId(),
                propiedad.getTitulo(),
                propiedad.getUbicacion(),
                propiedad.getPrecioPorNoche(),
                propiedad.getHuespedes(),
                propiedad.getCalificacion(),
                propiedad.getImages().isEmpty() ? null : propiedad.getImages().get(0).getUrl(),
                propiedad.getMoneda()
        );
    }

    public PropiedadDetailDTO getPropiedadDetail(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        List<String> imageUrls = propiedad.getImages().stream()
                .map(PropiedadImagen::getUrl)
                .collect(Collectors.toList());

        return new PropiedadDetailDTO(
                propiedad.getId(),
                propiedad.getTitulo(),
                propiedad.getDescripcion(),
                propiedad.getUbicacion(),
                propiedad.getPrecioPorNoche(),
                propiedad.getHuespedes(),
                propiedad.getHabitaciones(),
                propiedad.getBanos(),
                propiedad.getCalificacion(),
                imageUrls,
                propiedad.getMoneda()
        );
    }
    
    // Verifica si una propiedad está disponible en un rango de fechas
   private boolean isPropiedadDisponible(Long propiedadId, LocalDate fechaInicio, LocalDate fechaFin) {
       Timestamp inicioTS = Timestamp.valueOf(fechaInicio.atStartOfDay());
       Timestamp finTS = Timestamp.valueOf(fechaFin.atTime(23,59,59));
       return !reservaRepository.existsReservaWithPassedDates(propiedadId, inicioTS, finTS);
   }
   
}
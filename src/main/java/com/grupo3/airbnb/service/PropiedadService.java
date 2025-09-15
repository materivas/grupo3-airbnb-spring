package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    // Método nuevo
    public Propiedad getPropiedad(Long id) {
        return propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    public List<PropiedadListDTO> getAllPropiedades(Double precioMin, Double precioMax, String moneda) {
        List<Propiedad> propiedades = propiedadRepository.findAll();

        return propiedades.stream()
                .filter(propiedad -> {
                    boolean matchesPrice = true;
                    if (precioMin != null) {
                        matchesPrice = propiedad.getPrecioPorNoche() >= precioMin;
                    }
                    if (precioMax != null && matchesPrice) {
                        matchesPrice = propiedad.getPrecioPorNoche() <= precioMax;
                    }
                    if (moneda != null && !moneda.isEmpty()) {
                        matchesPrice = matchesPrice && moneda.equals(propiedad.getMoneda());
                    }
                    return matchesPrice;
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
}
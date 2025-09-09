package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadImagenRepository;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    @Autowired
    private IPropiedadImagenRepository propiedadImagenRepository;

    public List<PropiedadListDTO> getAllPropiedades() {
        List<Propiedad> propiedades = propiedadRepository.findAll();
        return propiedades.stream()
                .map(this::convertToPropiedadListDTO)
                .collect(Collectors.toList());
    }

    public PropiedadDetailDTO getPropiedadDetail(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        List<PropiedadImagen> imagenes = propiedadImagenRepository.findByPropiedadId(id);
        List<String> imageUrls = imagenes.stream()
                .map(PropiedadImagen::getUrl)
                .collect(Collectors.toList());

        return convertToPropiedadDetailDTO(propiedad, imageUrls);
    }

    public Propiedad getPropiedad(Long id) {
        return propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    // Métodos de conversión
    private PropiedadListDTO convertToPropiedadListDTO(Propiedad propiedad) {
        String mainImageUrl = propiedad.getImages().isEmpty() ?
                null : propiedad.getImages().get(0).getUrl();

        return new PropiedadListDTO(
                propiedad.getId(),
                propiedad.getTitulo(),
                propiedad.getUbicacion(),
                propiedad.getPrecioPorNoche(),
                propiedad.getHuespedes(),
                propiedad.getCalificacion(),
                mainImageUrl
        );
    }

    private PropiedadDetailDTO convertToPropiedadDetailDTO(Propiedad propiedad, List<String> imageUrls) {
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
                imageUrls
        );
    }
}
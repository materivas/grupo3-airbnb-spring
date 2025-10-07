package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.service.PropiedadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "*")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;
    
    @GetMapping
    public ResponseEntity<List<PropiedadListDTO>> getAllPropiedades(
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String moneda,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate fechaMin,
            @RequestParam(required = false) LocalDate fechaMax) {
        try {
            List<PropiedadListDTO> propiedades = propiedadService.getAllPropiedades(precioMin, precioMax, moneda, location, fechaMin, fechaMax);
            return ResponseEntity.ok(propiedades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDetailDTO> getPropiedadDetail(@PathVariable Long id) {
        try {
            PropiedadDetailDTO propiedadDetail = propiedadService.getPropiedadDetail(id);
            return ResponseEntity.ok(propiedadDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lugares/buscar")
    public ResponseEntity<List<String>> searchPlaces(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty())
                return ResponseEntity.ok(List.of());

            // Traigo todas las ubicaciones desde la base de datos
            List<String> allPlaces = propiedadService.getAllPropiedades(null,null,null,null,null,null)
                    .stream()
                    .map(PropiedadListDTO::getUbicacion)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();

            // Se filtra por coincidencia y se limita a 5 resultados
            List<String> matchingPlaces = allPlaces.stream()
                    .filter(p -> p.toLowerCase().contains(query.toLowerCase()))
                    .limit(5l)
                    .toList();

            return ResponseEntity.ok(matchingPlaces);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

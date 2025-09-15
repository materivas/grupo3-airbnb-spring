package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.service.PropiedadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
            @RequestParam(required = false) LocalDate fechaMin,
            @RequestParam(required = false) LocalDate fechaMax) {
        try {
            List<PropiedadListDTO> propiedades = propiedadService.getAllPropiedades(precioMin, precioMax, moneda, fechaMin, fechaMax);
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
}

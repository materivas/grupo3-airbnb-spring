package com.grupo3.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.service.PropiedadService;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "*")
public class PropiedadController {

	@Autowired
	private PropiedadService propiedadService;
	
	@GetMapping
	public ResponseEntity<List<PropiedadListDTO>> getAllPropiedades() {
        try {
            List<PropiedadListDTO> propiedades = propiedadService.getAllPropiedades();
            return ResponseEntity.ok(propiedades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	 // GET: /api/propiedades/{id} - Ver detalles de una propiedad específica
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

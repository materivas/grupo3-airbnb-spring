package com.grupo3.airbnb.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Getter @Setter
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double precioPorNoche;
    private Integer huespedes;
    private Integer habitaciones;
    private Integer banos;
    private Double calificacion;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropiedadImagen> images = new ArrayList<>();


    // Añadir usuarios/hosts/reservas proximamente


}

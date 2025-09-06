package com.grupo3.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

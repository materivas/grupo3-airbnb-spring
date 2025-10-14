package com.grupo3.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anfitrion_id")
    private Anfitrion anfitrion;

    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double precioPorNoche;
    private Integer huespedes;
    private Integer habitaciones;
    private Integer banos;
    private Double calificacion;
    private String moneda = "USD"; // Nuevo campo para la moneda

    @Enumerated(EnumType.STRING)
    private PropiedadEstado estado = PropiedadEstado.PUBLICADA;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropiedadImagen> images = new ArrayList<>();

    // Constructores, getters y setters
    public Propiedad() {
    }

    public Propiedad(Anfitrion anfitrion, String titulo, String descripcion, String ubicacion,
            Double precioPorNoche, Integer huespedes, Integer habitaciones,
            Integer banos, Double calificacion, String moneda) {
        this.anfitrion = anfitrion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.precioPorNoche = precioPorNoche;
        this.huespedes = huespedes;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.calificacion = calificacion;
        this.moneda = moneda;
    }
}
package com.grupo3.airbnb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PropiedadDetailDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double precioPorNoche;
    private Integer huespedes;
    private Integer habitaciones;
    private Integer banos;
    private Double calificacion;
    private List<String> imageUrls;
    private String moneda; // Nuevo campo

    public PropiedadDetailDTO(Long id, String titulo, String descripcion, String ubicacion,
                              Double precioPorNoche, Integer huespedes, Integer habitaciones,
                              Integer banos, Double calificacion, List<String> imageUrls, String moneda) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.precioPorNoche = precioPorNoche;
        this.huespedes = huespedes;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.calificacion = calificacion;
        this.imageUrls = imageUrls;
        this.moneda = moneda;
    }
}

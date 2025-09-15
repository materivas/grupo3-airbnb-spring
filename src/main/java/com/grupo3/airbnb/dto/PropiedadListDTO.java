package com.grupo3.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadListDTO {
    private Long id;
    private String titulo;
    private String ubicacion;
    private Double precioPorNoche;
    private Integer huespedes;
    private Double calificacion;
    private String mainImageUrl;
    private String moneda; // Nuevo campo
}

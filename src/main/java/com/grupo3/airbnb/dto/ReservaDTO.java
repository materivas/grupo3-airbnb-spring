package com.grupo3.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private String propiedadTitulo;
    private int nroHuespedes;
    private String huesped; // De momento es un String, pero debería ser un Usuario autenticado
    private java.sql.Timestamp entrada;
    private java.sql.Timestamp salida;
    private double precioTotal;
}

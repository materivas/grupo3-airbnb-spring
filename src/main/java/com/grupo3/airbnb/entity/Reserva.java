package com.grupo3.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Propiedad propiedad;
    private int nroHuespedes;
    //TODO: deberia ser un usuario autenticado
//    @ManyToOne
//    private Usuario huesped;
    private String huesped;
    private Timestamp entrada;
    private Timestamp salida;
    private double precioTotal;
}

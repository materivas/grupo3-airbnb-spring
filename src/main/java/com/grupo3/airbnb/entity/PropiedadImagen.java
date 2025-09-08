package com.grupo3.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PropiedadImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;  // URL de la imagen

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Propiedad propiedad;


}

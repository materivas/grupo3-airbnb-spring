package com.grupo3.airbnb.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    private String comentario;
    
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double calificacionGeneral;
    
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double calificacionLimpieza;
    
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double calificacionUbicacion;
    
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double calificacionComunicacion;
    
    private Boolean publicada = false;
    
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaPublicacion;
    
    private String usuario; // Usuario como string
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;
    
}
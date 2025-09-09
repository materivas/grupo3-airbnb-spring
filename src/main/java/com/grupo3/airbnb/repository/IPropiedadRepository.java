package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findAll();

    Optional<Propiedad> findById(Long id);
}

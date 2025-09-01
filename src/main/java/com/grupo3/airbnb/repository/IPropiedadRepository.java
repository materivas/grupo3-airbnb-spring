package com.grupo3.airbnb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.airbnb.entity.Propiedad;

public interface IPropiedadRepository extends JpaRepository<Propiedad, Long>{
	
	List<Propiedad> findAll();
    
	Optional<Propiedad> findById(Long id);
}

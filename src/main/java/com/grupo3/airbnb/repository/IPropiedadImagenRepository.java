package com.grupo3.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupo3.airbnb.entity.PropiedadImagen;

public interface IPropiedadImagenRepository extends JpaRepository<PropiedadImagen, Long>  {

	 @Query("SELECT pi FROM PropiedadImagen pi WHERE pi.propiedad.id = :propiedadId")
	 List<PropiedadImagen> findByPropiedadId(@Param("propiedadId") Long propiedadId);
	 
}

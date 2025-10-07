package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findAll();

    Optional<Propiedad> findById(Long id);

    // O si necesitas una consulta más específica:
    @Query("SELECT p FROM Propiedad p WHERE p.anfitrion.dni = :anfitrionDni ORDER BY p.id DESC")
    List<Propiedad> findPropiedadesByAnfitrionDni(@Param("anfitrionDni") Long anfitrionDni);
}

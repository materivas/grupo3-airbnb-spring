package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Anfitrion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IAnfitrionRepository extends JpaRepository<Anfitrion, Long> {

    @Query("SELECT a FROM Anfitrion a WHERE a.dni= :anfitrionDni")
    Optional<Anfitrion> findByDni(@Param("anfitrionDni") Long dni);
}

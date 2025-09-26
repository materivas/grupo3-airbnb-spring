package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByHuesped(String usuarioId);

    List<Reserva> findAllByHuesped(String huesped);

    List<Reserva> findByHuespedIgnoreCase(String huesped);

    @Query("SELECT DISTINCT r.huesped FROM Reserva r")
    List<String> findDistinctHuespedes();
    

    @Query("SELECT COUNT(r) > 0 FROM Reserva r " +
    	       "WHERE r.propiedad.id = :propiedadId " +
    	       "AND r.salida >= :inicioTS " +
    	       "AND r.entrada <= :finTS")
    	boolean existsReservaWithPassedDates(@Param("propiedadId") Long propiedadId,
    	                                     @Param("inicioTS") Timestamp inicioTS,
    	                                     @Param("finTS") Timestamp finTS);
    
}

package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByHuesped(String usuarioId);

    List<Reserva> findAllByHuesped(String huesped);

    List<Reserva> findByHuespedIgnoreCase(String huesped);

    @Query("SELECT DISTINCT r.huesped FROM Reserva r")
    List<String> findDistinctHuespedes();
}

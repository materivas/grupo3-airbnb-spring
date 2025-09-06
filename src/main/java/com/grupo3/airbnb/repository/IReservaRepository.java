package com.grupo3.airbnb.repository;

import com.grupo3.airbnb.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IReservaRepository extends JpaRepository<Reserva,Long> {

    Optional<Reserva> findByHuesped(String usuarioId);
}

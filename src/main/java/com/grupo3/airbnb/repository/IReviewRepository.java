package com.grupo3.airbnb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupo3.airbnb.entity.Review;

//repositories/ReviewRepository.java
public interface IReviewRepository extends JpaRepository<Review, Long> {
 
	 Optional<Review> findByReservaIdAndUsuario(Long reservaId, String usuario);

	 List<Review> findByPropiedadIdAndPublicadaTrue(Long propiedadId);

	 @Query("SELECT AVG(r.calificacionGeneral) FROM Review r WHERE r.propiedad.id = :propiedadId AND r.publicada = true")
	 Double findAverageRatingByPropiedadId(@Param("propiedadId") Long propiedadId);

	@Query("SELECT COUNT(r) FROM Review r WHERE r.propiedad.id = :propiedadId AND r.publicada = true")
	Long countByPropiedadIdAndPublicadaTrue(@Param("propiedadId") Long propiedadId);
}
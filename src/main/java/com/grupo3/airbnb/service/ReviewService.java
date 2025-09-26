package com.grupo3.airbnb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.entity.Review;
import com.grupo3.airbnb.repository.IReviewRepository;

@Service
public class ReviewService {
 
    @Autowired
    private IReviewRepository reviewRepository;
 
    @Autowired
    private ReservaService reservaService;
 
    // Crear una nueva review
    public Review crearReview(Review review, Long reservaId, String usuario) {
        Reserva reserva = reservaService.getReserva(reservaId);
        
        review.setReserva(reserva);
        review.setPropiedad(reserva.getPropiedad());
        review.setUsuario(usuario);
        review.setPublicada(true); // Publicar directamente sin confirmación
        review.setFechaCreacion(LocalDateTime.now());
        review.setFechaPublicacion(LocalDateTime.now());
        
        return reviewRepository.save(review);
    }
    

    // Confirmar y publicar review
    public Review confirmarYPublicarReview(Long reviewId, String usuario) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
         
        if (!review.getUsuario().equals(usuario)) {
            throw new IllegalStateException("No tienes permiso para publicar esta reseña");
        }
         
        review.setPublicada(true);
        review.setFechaPublicacion(LocalDateTime.now());
     
        return reviewRepository.save(review);
    }
 
    public Review obtenerReviewPorId(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }
 
    public List<Review> obtenerReviewsPublicadasDePropiedad(Long propiedadId) {
        return reviewRepository.findByPropiedadIdAndPublicadaTrue(propiedadId);
    }
 
    // Verifica si se puede hacer una review
    public boolean puedeHacerReview(Long reservaId, String usuario) {
        // SOLO validación básica: que la reserva exista y pertenezca al usuario
        try {
            Reserva reserva = reservaService.getReserva(reservaId);
            return reserva.getHuesped().equals(usuario);
        } catch (Exception e) {
            return false;
        }
    }

 
    // Obtiene las reservas reseñables por un usuario
    public List<ReservaDTO> getReservasParaReview(String usuario) {
        List<ReservaDTO> reservasUsuario = reservaService.getReservasByUsuario(usuario);
     
        return reservasUsuario.stream()
            .filter(reservaDTO -> {
                try {
                    Reserva reserva = reservaService.getReserva(reservaDTO.getId());
                    LocalDate fechaCheckout = reserva.getSalida().toLocalDateTime().toLocalDate();
                 
                    return fechaCheckout.isBefore(LocalDate.now()) &&
                           reviewRepository.findByReservaIdAndUsuario(reservaDTO.getId(), usuario).isEmpty();
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }
 
    // Rating promedio de una propiedad
    public Double calcularRatingPromedio(Long propiedadId) {
        Double promedio = reviewRepository.findAverageRatingByPropiedadId(propiedadId);
        return promedio != null ? Math.round(promedio * 10.0) / 10.0 : 0.0;
    }
 
    // Estadísticas de reviews
    public Map<String, Object> getEstadisticasReviews(Long propiedadId) {
        Map<String, Object> estadisticas = new HashMap<>();
     
        Double promedio = calcularRatingPromedio(propiedadId);
        Long totalReviews = reviewRepository.countByPropiedadIdAndPublicadaTrue(propiedadId);
     
        estadisticas.put("promedio", promedio);
        estadisticas.put("totalReviews", totalReviews);
     
        return estadisticas;
    }
}

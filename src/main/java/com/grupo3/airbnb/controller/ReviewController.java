package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Review;
import com.grupo3.airbnb.service.ReservaService;
import com.grupo3.airbnb.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReservaService reservaService;

    // LISTA DE RESERVAS DISPONIBLES PARA REVIEW
    @GetMapping("/disponibles")
    public String listarReservasParaReview(
            @RequestParam(required = false) String usuario,
            Model model) {

        if (usuario == null || usuario.trim().isEmpty()) {
            model.addAttribute("error", "Debe ingresar un nombre de usuario válido.");
            return "lista-reservas-review";
        }

        try {
            List<ReservaDTO> reservasParaReview = reviewService.getReservasParaReview(usuario);

            if (reservasParaReview.isEmpty()) {
                model.addAttribute("mensaje", "No hay reservas disponibles para reseñar.");
            } else {
                model.addAttribute("reservas", reservasParaReview);
            }

            model.addAttribute("usuario", usuario);

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar reservas: " + e.getMessage());
        }

        return "reviews/lista-reservas-review";
    }

    // US1: MOSTRAR FORMULARIO DE REVIEW
    @GetMapping("/nueva/{reservaId}")
    public String mostrarFormularioReview(
            @PathVariable Long reservaId,
            @RequestParam String usuario,
            Model model) {

        try {
            if (!reviewService.puedeHacerReview(reservaId, usuario)) {
                model.addAttribute("error", "No puedes escribir una reseña para esta reserva");
                return "error";
            }

            var reserva = reservaService.getReserva(reservaId);
            model.addAttribute("reserva", reserva);
            model.addAttribute("review", new Review());
            model.addAttribute("usuario", usuario);

            return "formulario-review";

        } catch (Exception e) {
            model.addAttribute("error", "Reserva no encontrada: " + e.getMessage());
            return "error";
        }
    }

    // US1: PROCESAR FORMULARIO DE REVIEW
    @PostMapping("/crear")
    public String crearReview(
            @ModelAttribute Review review,
            @RequestParam Long reservaId,
            @RequestParam String usuario,
            RedirectAttributes redirectAttributes) {

        try {
            Review reviewCreada = reviewService.crearReview(review, reservaId, usuario);
            
            // US3: Redirigir directamente a éxito
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Reseña publicada exitosamente!");
            return "redirect:/reviews/exito/" + reviewCreada.getId() + "?usuario=" + usuario;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reviews/nueva/" + reservaId + "?usuario=" + usuario;
        }
    }


    // US2: MOSTRAR CONFIRMACIÓN
    @GetMapping("/confirmar/{reviewId}")
    public String mostrarConfirmacion(
            @PathVariable Long reviewId,
            @RequestParam String usuario,
            Model model) {

        try {
            Review review = reviewService.obtenerReviewPorId(reviewId);

            if (!review.getUsuario().equals(usuario)) {
                model.addAttribute("error", "No tienes permiso para ver esta reseña");
                return "error";
            }

            model.addAttribute("review", review);
            model.addAttribute("usuario", usuario);
            return "reviews/confirmacion-review";

        } catch (Exception e) {
            model.addAttribute("error", "Reseña no encontrada: " + e.getMessage());
            return "error";
        }
    }

    // US2: CONFIRMAR Y PUBLICAR
    @PostMapping("/publicar/{reviewId}")
    public String publicarReview(
            @PathVariable Long reviewId,
            @RequestParam String usuario,
            RedirectAttributes redirectAttributes) {

        try {
            Review reviewPublicada = reviewService.confirmarYPublicarReview(reviewId, usuario);

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Tu reseña se publicó exitosamente!");
            redirectAttributes.addAttribute("usuario", usuario);
            return "redirect:/reviews/exito/" + reviewPublicada.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addAttribute("usuario", usuario);
            return "redirect:/reviews/confirmar/" + reviewId;
        }
    }

    // US3: MOSTRAR ÉXITO
    @GetMapping("/exito/{reviewId}")
    public String mostrarExito(
            @PathVariable Long reviewId,
            @RequestParam String usuario,
            Model model) {

        Review review = reviewService.obtenerReviewPorId(reviewId);
        model.addAttribute("review", review);
        model.addAttribute("usuario", usuario);

        return "exito-review";
    }
}

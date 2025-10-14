package com.grupo3.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo3.airbnb.entity.HostGuestReview;
import com.grupo3.airbnb.service.HostGuestReviewService;
import com.grupo3.airbnb.service.ReservaService;

@Controller
public class HostGuestReviewController {

    @Autowired
    private HostGuestReviewService service;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/anfitrion/comentario/nuevo")
    public String nuevoComentario(@RequestParam Long reservaId, @RequestParam Long anfitrionDni, Model model) {
        var reserva = reservaService.getReserva(reservaId);
        model.addAttribute("reserva", reserva);
        model.addAttribute("anfitrionDni", anfitrionDni);
        return "comentario-huesped";
    }

    @PostMapping("/anfitrion/comentario")
    public String crearComentario(@RequestParam Long reservaId,
                                  @RequestParam Long anfitrionDni,
                                  @RequestParam String comentario,
                                  Model model) {
        if (comentario == null || comentario.trim().isEmpty()) {
            model.addAttribute("error", "El comentario no puede estar vacío");
            return nuevoComentario(reservaId, anfitrionDni, model);
        }
        if (comentario.length() > 500) {
            model.addAttribute("error", "El comentario no puede exceder 500 caracteres");
            return nuevoComentario(reservaId, anfitrionDni, model);
        }
        var creado = service.crear(anfitrionDni, reservaId, comentario.trim());
        return "redirect:/huesped/" + creado.getHuesped() + "/comentarios";
    }

    @GetMapping("/huesped/{usuario}/comentarios")
    public String comentariosDeHuesped(@PathVariable String usuario, Model model) {
        List<HostGuestReview> comentarios = service.listarPorHuesped(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("comentarios", comentarios);
        return "comentarios-huesped";
    }
}


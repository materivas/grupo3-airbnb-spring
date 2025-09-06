package com.grupo3.airbnb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/reservar/{propiedadId}")
    public String reservar( @PathVariable() String propiedadId, Model model) {
        model.addAttribute("propiedadId", propiedadId);
        return "reservar";
    }

    @GetMapping("/publicar")
    public String publicar() {
        return "publicar"; // Próximo 
    }

    @GetMapping("/mis-reservas")
    public String misReservas(@ModelAttribute String usuario, ModelMap model) {
        model.addAttribute("usuario", usuario);
        return "listadoReservas"; // Corregir, tira error cargando el objeto reserva
    }

    @GetMapping("/propiedad/{id}")
    public String propiedadDetail(@PathVariable Long id, Model model) {
        model.addAttribute("propiedadId", id);
        return "propiedad-detail";
    }
}

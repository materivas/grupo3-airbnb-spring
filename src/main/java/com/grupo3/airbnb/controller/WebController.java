package com.grupo3.airbnb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/publicar")
    public String publicar() {
        return "publicar"; // Próximo 
    }

    @GetMapping("/mis-reservas")
    public String misReservas() {
        return "listadoReservas"; // Próximo
    }

    @GetMapping("/propiedad/{id}")
    public String propiedadDetail(@PathVariable Long id, Model model) {
        model.addAttribute("propiedadId", id);
        return "propiedad-detail";
    }
}

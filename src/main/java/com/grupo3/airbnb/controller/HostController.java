package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.service.AnfitrionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HostController {

    @Autowired
    private AnfitrionService anfitrionService;

    @GetMapping("/anfitrion/registro")
    public String mostrarRegistro() {
        return "registrar-anfitrion";
    }

    @PostMapping("/anfitrion/registro")
    public String procesarRegistro(
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String identificacionFiscal,
            @RequestParam(name = "aceptaTerminos", defaultValue = "false") boolean aceptaTerminos,
            Model model
    ) {
        try {
            if (!aceptaTerminos) {
                model.addAttribute("error", "Debe aceptar los Términos y Condiciones de Anfitrión");
                return "registrar-anfitrion";
            }
            if (email == null || !email.contains("@")) {
                model.addAttribute("error", "Email inválido");
                return "registrar-anfitrion";
            }
            if (telefono == null || telefono.trim().length() < 6) {
                model.addAttribute("error", "Teléfono inválido");
                return "registrar-anfitrion";
            }
            if (identificacionFiscal == null || identificacionFiscal.trim().length() < 5) {
                model.addAttribute("error", "Identificación fiscal inválida");
                return "registrar-anfitrion";
            }

            Anfitrion existente = anfitrionService.findByDni(dni);
            if (existente != null) {
                existente.setNombre(nombre);
                existente.setApellido(apellido);
                existente.setEmail(email);
                existente.setTelefono(telefono);
                existente.setIdentificacionFiscal(identificacionFiscal);
                anfitrionService.save(existente);
            } else {
                anfitrionService.crearAnfitrion(dni, nombre, apellido, email, telefono, identificacionFiscal);
            }

            return "redirect:/mis-propiedades?anfitrion=" + dni;
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "registrar-anfitrion";
        }
    }
}


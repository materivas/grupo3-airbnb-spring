package com.grupo3.airbnb.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Review;
import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.service.AnfitrionService;
import com.grupo3.airbnb.service.PropiedadService;
import com.grupo3.airbnb.service.ReservaService;
import com.grupo3.airbnb.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private AnfitrionService anfitrionService;

    // PÁGINA PRINCIPAL
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // MOSTRAR FORMULARIO DE RESERVA
    @GetMapping("/reservar/{propiedadId}")
    public String mostrarFormularioReserva(@PathVariable String propiedadId, Model model) {
        model.addAttribute("propiedadId", propiedadId);
        return "reservar";
    }

    // PROCESAR FORMULARIO DE RESERVA
    @PostMapping("/reservar/{propiedadId}")
    public String procesarReserva(
            @PathVariable String propiedadId,
            @RequestParam int nroHuespedes,
            @RequestParam String usuario,
            @RequestParam String fechaEntrada,
            @RequestParam String fechaSalida,
            @RequestParam String metodoPago,
            @RequestParam(required = false) String numeroTarjeta,
            @RequestParam(required = false) String fechaExpiracion,
            @RequestParam(required = false) String cvv,
            @RequestParam(required = false) String titularTarjeta,
            Model model) {

        try {
            // Convertir fechas
            LocalDate entrada = LocalDate.parse(fechaEntrada);
            LocalDate salida = LocalDate.parse(fechaSalida);

            // Validar método de pago si es tarjeta
            if ("tarjeta".equals(metodoPago)) {
                if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) {
                    throw new IllegalArgumentException("Número de tarjeta requerido");
                }
                // Aquí podrías agregar más validaciones para la tarjeta
            }

            // Crear reserva
            reservaService.createReserva(nroHuespedes, entrada, salida, String.valueOf(Long.parseLong(propiedadId)),
                    usuario);

            // Redirigir a mis reservas
            return "redirect:/mis-reservas?usuario=" + usuario;

        } catch (Exception e) {
            // Si hay error, volver al formulario
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("propiedadId", propiedadId);
            return "reservar";
        }
    }

    // MIS RESERVAS
    @GetMapping("/mis-reservas")
    public String misReservas(@RequestParam(required = false) String usuario, Model model) {

        if (usuario == null || usuario.trim().isEmpty()) {
            // Mostrar formulario de búsqueda
            model.addAttribute("reservas", List.of());
            return "mis-reservas";
        }

        // Mostrar reservas del usuario
        List<ReservaDTO> reservas = reservaService.getReservasByUsuario(usuario);
        model.addAttribute("reservas", reservas);
        model.addAttribute("usuario", usuario);
        return "mis-reservas";
    }

    // DETALLE DE PROPIEDAD
    @GetMapping("/propiedad/{id}")
    public String propiedadDetail(@PathVariable Long id, Model model) {
        model.addAttribute("propiedadId", id);

        // Traer reseñas publicadas
        List<Review> reviews = reviewService.obtenerReviewsPublicadasDePropiedad(id);
        model.addAttribute("reviews", reviews);

        return "propiedad-detail";
    }

    // PUBLICAR
    @GetMapping("/publicar")
    public String publicar() {
        return "publicar-propiedad";
    }

    // PROCESAR PUBLICACION
    @PostMapping("/propiedad/publicar")
    public String procesarPublicarPropiedad(
            @RequestParam Long anfitrionDni,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String ubicacion,
            @RequestParam int nroHuespedes,
            @RequestParam int nroHabitaciones,
            @RequestParam int nroBanios,
            @RequestParam Double precioPorNoche,
            @RequestParam String moneda,
            Model model) {
        try {
            // validaciones
            Propiedad p = propiedadService.createPropiedad(anfitrionDni, titulo, descripcion, ubicacion, precioPorNoche,
                    moneda, nroHuespedes, nroHabitaciones, nroBanios);
            Anfitrion a = p.getAnfitrion();
            return "redirect:/mis-propiedades?anfitrion=" + a.getDni();
        } catch (Exception e) {
            // Si hay error, volver al formulario
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("anfitrionDni", anfitrionDni);
            return "publicar-propiedad";
        }
    }

    // MIS PROPIEDADES
    @GetMapping("/mis-propiedades")
    public String misPropiedades(@RequestParam(required = false) Long anfitrion, Model model) {
        if (anfitrion == null || anfitrion < 0) {
            // Mostrar formulario de búsqueda
            model.addAttribute("propiedades", List.of());
            return "mis-propiedades";
        }

        // Mostrar propiedades del usuario
        List<PropiedadListDTO> propiedades = propiedadService.getPropiedadesByAnfitrion(anfitrion);
        model.addAttribute("propiedades", propiedades);
        model.addAttribute("anfitrion", anfitrion);
        return "mis-propiedades";
    }

    @GetMapping("/mis-reviews")
    public String misReviews(@RequestParam(required = false) String usuario, Model model) {
        if (usuario == null || usuario.trim().isEmpty()) {
            model.addAttribute("usuario", "");
            return "redirect:/reviews/disponibles";
        }

        return "redirect:/reviews/disponibles?usuario=" + usuario;
    }

    @GetMapping("/api/anfitriones/{dni}")
    @ResponseBody
    public ResponseEntity<?> verificarAnfitrion(@PathVariable Long dni) {
        try {
            Anfitrion anfitrion = anfitrionService.findByDni(dni);
            if (anfitrion != null) {
                return ResponseEntity.ok().build(); // Anfitrión existe
            } else {
                return ResponseEntity.notFound().build(); // Anfitrión no existe
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
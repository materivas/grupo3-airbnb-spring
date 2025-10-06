package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.ReservaDTO;
import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.service.PropiedadService;
import com.grupo3.airbnb.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PropiedadService propiedadService;

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
            reservaService.createReserva(nroHuespedes, entrada, salida, String.valueOf(Long.parseLong(propiedadId)), usuario);

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
        return "propiedad-detail";
    }

    // PUBLICAR
    @GetMapping("/publicar")
    public String publicar() {
        return "publicar-propiedad";
    }

    //PROCESAR PUBLICACION
    @PostMapping("/propiedad/publicar/{anfitrionDni}")
    public String procesarPublicarPropiedad(
            @PathVariable Long anfitrionDni,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String ubicacion,
            @RequestParam int nroHuespedes,
            @RequestParam int nroHabitaciones,
            @RequestParam int nroBanios,
            @RequestParam Double precioPorNoche,
            @RequestParam String moneda,
            Model model
    ){
        try {
            //validaciones
            Propiedad p= propiedadService.createPropiedad(anfitrionDni,titulo, descripcion, ubicacion,precioPorNoche, moneda,
                    nroHuespedes, nroHabitaciones, nroBanios);
            Anfitrion a= p.getAnfitrion();
            return "redirect:/mis-propiedades?anfitrion=" + a.getDni();
        } catch (Exception e) {
            // Si hay error, volver al formulario
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("anfitrionDni", anfitrionDni);
            return "publicar";
        }
    }

    //MIS PROPIEDADES
    @GetMapping("/mis-propiedades")
    public String misPropiedades(@RequestParam(required = false) Long dni, Model model) {

//        if (dni == null || dni <0 ) {
//            // Mostrar formulario de búsqueda
//            model.addAttribute("propiedades", List.of());
//            return "mis-propiedades";
//        }

//        // Mostrar propiedades del usuario
//        List<PropiedadListDTO> propiedades = propiedadService.getPropiedadesByAnfitrion(dni);
//        model.addAttribute("propiedades", propiedades);
//        model.addAttribute("anfitrion", dni);
//        return "mis-propiedades";
        return "a implementar";
    }
}
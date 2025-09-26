package com.grupo3.airbnb.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja excepciones genéricas
    @ExceptionHandler(Exception.class)
    public String manejarExcepcionesGenerales(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage() != null ? ex.getMessage() : "Ha ocurrido un error inesperado.");
        return "error"; // apunta a error.html en templates
    }

    @ExceptionHandler(IllegalStateException.class)
    public String manejarIllegalState(IllegalStateException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}

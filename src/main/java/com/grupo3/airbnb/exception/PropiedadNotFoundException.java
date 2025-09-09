package com.grupo3.airbnb.exception;

@SuppressWarnings("serial")
public class PropiedadNotFoundException extends RuntimeException {
    public PropiedadNotFoundException(String message) {
        super(message);
    }
}

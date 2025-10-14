package com.grupo3.airbnb.service;

import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.repository.IAnfitrionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnfitrionService {
    @Autowired
    private IAnfitrionRepository anfitrionRepository;

    public Anfitrion crearAnfitrion(Long dni, String nombre, String apellido) {
        Anfitrion a = new Anfitrion();
        a.setDni(dni);
        a.setNombre(nombre);
        a.setApellido(apellido);
        return anfitrionRepository.save(a);
    }

    // Sobrecarga con datos de contacto
    public Anfitrion crearAnfitrion(Long dni, String nombre, String apellido, String email, String telefono, String identificacionFiscal) {
        Anfitrion a = new Anfitrion();
        a.setDni(dni);
        a.setNombre(nombre);
        a.setApellido(apellido);
        a.setEmail(email);
        a.setTelefono(telefono);
        a.setIdentificacionFiscal(identificacionFiscal);
        return anfitrionRepository.save(a);
    }

    public Anfitrion getAnfitrion(Long id) {
        return anfitrionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anfitrion no encontrado"));
    }

    public Anfitrion getAnfitrionByDni(Long dni) {
        return anfitrionRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Anfitrion no encontrado"));
    }

    // para una futura busqueda
    public List<Propiedad> getPropiedadesByAnfitrion(Long dni) {
        Anfitrion a = getAnfitrionByDni(dni);
        return a.getPropiedades();
    }

    public Anfitrion findByDni(Long dni) {
        return anfitrionRepository.findByDni(dni)
                .orElse(null);
    }

    public Anfitrion save(Anfitrion anfitrion) {
        return anfitrionRepository.save(anfitrion);
    }
}

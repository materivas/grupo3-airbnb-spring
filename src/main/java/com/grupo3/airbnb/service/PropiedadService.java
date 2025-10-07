package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import com.grupo3.airbnb.repository.IReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropiedadService {

    @Autowired
    private IPropiedadRepository propiedadRepository;
    @Autowired
    private IReservaRepository reservaRepository;
    @Autowired
    private AnfitrionService anfitrionService;

    public Propiedad createPropiedad(Long anfitrionDni, String titulo, String descripcion,
            String ubicacion, Double precioPorNoche, String moneda,
            Integer nroHuespedes, Integer nroHabitaciones, Integer nroBanios) {

        try {
            // 1. Buscar anfitrión existente
            Anfitrion anfitrion = anfitrionService.findByDni(anfitrionDni);

            // 2. Si no existe, crear uno nuevo
            if (anfitrion == null) {
                anfitrion = new Anfitrion();
                anfitrion.setDni(anfitrionDni);
                anfitrion.setNombre("Anfitrión " + anfitrionDni); // Nombre por defecto
                anfitrion = anfitrionService.save(anfitrion); // Guardar en la base de datos
            }

            // 3. Crear propiedad - ¡ESTA ES LA PARTE QUE FALTA!
            Propiedad propiedad = new Propiedad();

            // ESTA LÍNEA ES LA MÁS IMPORTANTE - ASIGNAR EL ANFITRIÓN
            propiedad.setAnfitrion(anfitrion);

            propiedad.setTitulo(titulo);
            propiedad.setDescripcion(descripcion);
            propiedad.setUbicacion(ubicacion);
            propiedad.setPrecioPorNoche(precioPorNoche);
            propiedad.setMoneda(moneda);
            propiedad.setHuespedes(nroHuespedes);
            propiedad.setHabitaciones(nroHabitaciones);
            propiedad.setBanos(nroBanios);

            // 4. Guardar propiedad
            return propiedadRepository.save(propiedad);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Método nuevo
    public Propiedad getPropiedad(Long id) {
        return propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    public List<PropiedadListDTO> getAllPropiedades(Double precioMin, Double precioMax, String moneda,
            LocalDate fechaMin, LocalDate fechaMax) {

        List<Propiedad> propiedades = propiedadRepository.findAll();

        return propiedades.stream().filter(propiedad -> {

            // --- Filtro por precio mínimo ---
            if (precioMin != null && propiedad.getPrecioPorNoche() < precioMin) {
                return false;
            }

            // --- Filtro por precio máximo ---
            if (precioMax != null && propiedad.getPrecioPorNoche() > precioMax) {
                return false;
            }

            // --- Filtro por moneda ---
            if (moneda != null && !moneda.isEmpty() && !moneda.equals(propiedad.getMoneda())) {
                return false;
            }

            // --- Filtro por disponibilidad ---
            if (fechaMin != null || fechaMax != null) {
                LocalDate start = fechaMin != null ? fechaMin : fechaMax;
                LocalDate end = fechaMax != null ? fechaMax : fechaMin;

                if (!isPropiedadDisponible(propiedad.getId(), start, end)) {
                    return false;
                }
            }

            return true; // Pasa todos los filtros
        })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PropiedadListDTO> getPropiedadesByAnfitrion(Long anfitrionDni) {
        try {
            // Verificar que el anfitrión existe
            Anfitrion anfitrion = anfitrionService.getAnfitrionByDni(anfitrionDni);
            if (anfitrion == null) {
                throw new RuntimeException("Anfitrión no encontrado con DNI: " + anfitrionDni);
            }

            // Obtener propiedades del anfitrión
            List<Propiedad> propiedades = propiedadRepository.findPropiedadesByAnfitrionDni(anfitrionDni);

            // Convertir a DTO
            return propiedades.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener propiedades del anfitrión: " + e.getMessage());
        }
    }

    private PropiedadListDTO convertToDTO(Propiedad propiedad) {
        return new PropiedadListDTO(
                propiedad.getId(),
                propiedad.getTitulo(),
                propiedad.getUbicacion(),
                propiedad.getPrecioPorNoche(),
                propiedad.getHuespedes(),
                propiedad.getHabitaciones(),
                propiedad.getBanos(),
                propiedad.getCalificacion(),
                propiedad.getImages().isEmpty() ? null : propiedad.getImages().get(0).getUrl(),
                propiedad.getMoneda());
    }

    public PropiedadDetailDTO getPropiedadDetail(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        List<String> imageUrls = propiedad.getImages().stream()
                .map(PropiedadImagen::getUrl)
                .collect(Collectors.toList());

        return new PropiedadDetailDTO(
                propiedad.getId(),
                propiedad.getTitulo(),
                propiedad.getDescripcion(),
                propiedad.getUbicacion(),
                propiedad.getPrecioPorNoche(),
                propiedad.getHuespedes(),
                propiedad.getHabitaciones(),
                propiedad.getBanos(),
                propiedad.getCalificacion(),
                imageUrls,
                propiedad.getMoneda());
    }

    // Verifica si una propiedad está disponible en un rango de fechas
    private boolean isPropiedadDisponible(Long propiedadId, LocalDate fechaInicio, LocalDate fechaFin) {
        Timestamp inicioTS = Timestamp.valueOf(fechaInicio.atStartOfDay());
        Timestamp finTS = Timestamp.valueOf(fechaFin.atTime(23, 59, 59));
        return !reservaRepository.existsReservaWithPassedDates(propiedadId, inicioTS, finTS);
    }

}
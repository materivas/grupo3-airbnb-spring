package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import com.grupo3.airbnb.repository.IReservaRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropiedadServiceTest {

    @Mock
    private IPropiedadRepository propiedadRepository;
    @Mock
    private IReservaRepository reservaRepository;
    
    @InjectMocks
    private PropiedadService propiedadService;

    private Propiedad propiedad;
    private Propiedad propiedad2;

    @Before
    public void setUp() {
        propiedad = new Propiedad();
        propiedad.setId(1L);
        propiedad.setTitulo("Casa en la playa");
        propiedad.setDescripcion("Hermosa casa frente al mar");
        propiedad.setUbicacion("Cancún");
        propiedad.setPrecioPorNoche(150.00);
        propiedad.setHuespedes(6);
        propiedad.setHabitaciones(3);
        propiedad.setBanos(2);
        propiedad.setCalificacion(4.8);
        propiedad.setMoneda("USD");

        propiedad2 = new Propiedad();
        propiedad2.setId(2L);
        propiedad2.setTitulo("Departamento céntrico");
        propiedad2.setDescripcion("Departamento moderno en el centro");
        propiedad2.setUbicacion("Ciudad de México");
        propiedad2.setPrecioPorNoche(80.00);
        propiedad2.setHuespedes(4);
        propiedad2.setHabitaciones(2);
        propiedad2.setBanos(1);
        propiedad2.setCalificacion(4.5);
        propiedad2.setMoneda("USD");
    }

    @Test
    public void getAllPropiedades_WithoutFilters_ReturnsAllProperties() {
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(null, null, null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    public void getAllPropiedades_WithPriceFilter_ReturnsFilteredProperties() {
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(100.0, 200.0, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Casa en la playa", result.get(0).getTitulo());
    }

    @Test
    public void getAllPropiedades_WithCurrencyFilter_ReturnsFilteredProperties() {
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(null, null, "USD", null, null);

        assertEquals(2, result.size());
    }

    @Test
    public void getAllPropiedades_WithDatesRangeFilter_ReturnsFilteredProperties() {
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));
        when(reservaRepository.existsReservaWithPassedDates(any(), any(), any())).thenReturn(false);

        LocalDate fixedStart = LocalDate.of(2025, 9, 15);
        LocalDate fixedEnd = fixedStart.plusDays(5);

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(null, null, null, fixedStart, fixedEnd);

        assertEquals(2, result.size());
    }

    @Test
    public void getAllPropiedades_WithOnlyEndDate_ReturnsFilteredProperties() {
        // Arrange
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));
        when(reservaRepository.existsReservaWithPassedDates(any(), any(), any())).thenReturn(false);

        LocalDate endDate = LocalDate.of(2025, 9, 20);

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(null, null, null, null, endDate);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void getAllPropiedades_WithOnlyStartDate_ReturnsFilteredProperties() {
        // Arrange
        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad, propiedad2));
        when(reservaRepository.existsReservaWithPassedDates(any(), any(), any())).thenReturn(false);

        LocalDate startDate = LocalDate.of(2025, 9, 15);

        List<PropiedadListDTO> result = propiedadService.getAllPropiedades(null, null, null, startDate, null);

        assertEquals(2, result.size());
    }
    
    @Test
    public void getPropiedadDetail_ValidId_ReturnsPropertyDetail() {
        // Arrange
        PropiedadImagen imagen = new PropiedadImagen();
        imagen.setUrl("http://example.com/image.jpg");
        propiedad.setImages(Arrays.asList(imagen));

        when(propiedadRepository.findById(1L)).thenReturn(Optional.of(propiedad));

        // Act
        PropiedadDetailDTO result = propiedadService.getPropiedadDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Casa en la playa", result.getTitulo());
        assertEquals(1, result.getImageUrls().size());
    }

    @Test(expected = RuntimeException.class)
    public void getPropiedadDetail_InvalidId_ThrowsException() {
        // Arrange
        when(propiedadRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        propiedadService.getPropiedadDetail(999L);
    }

    @Test
    public void getPropiedad_ValidId_ReturnsProperty() {
        // Arrange
        when(propiedadRepository.findById(1L)).thenReturn(Optional.of(propiedad));

        // Act
        Propiedad result = propiedadService.getPropiedad(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Casa en la playa", result.getTitulo());
    }

    @Test(expected = RuntimeException.class)
    public void getPropiedad_InvalidId_ThrowsException() {
        // Arrange
        when(propiedadRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        propiedadService.getPropiedad(999L);
    }
}
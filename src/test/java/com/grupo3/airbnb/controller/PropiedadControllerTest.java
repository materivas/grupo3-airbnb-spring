package com.grupo3.airbnb.controller;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.service.PropiedadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PropiedadControllerTest {
    @Mock
    private PropiedadService propiedadService;
    @InjectMocks
    private PropiedadController propiedadController;

    @Test
    public void getAllPropertiesIsOkTest() {
        // Cambiar para incluir los parámetros nuevos
        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null, null)).thenReturn(new ArrayList<PropiedadListDTO>());

        // Cambiar para incluir los parámetros nuevos
        ResponseEntity<List<PropiedadListDTO>> actualResult = propiedadController.getAllPropiedades(null, null, null, null, null, null);
        assertEquals(new ArrayList<>(), actualResult.getBody());
    }

    @Test
    public void getAllPropertiesThrowsExceptionTest() {
        // Cambiar para incluir los parámetros nuevos
        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null, null)).thenThrow(new RuntimeException());

        // Cambiar para incluir los parámetros nuevos
        ResponseEntity<List<PropiedadListDTO>> actualResult = propiedadController.getAllPropiedades(null, null, null, null, null, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResult.getStatusCode());
        assertThat(actualResult.getBody()).isNull();
    }

    @Test
    public void getPropertyDetailIsOkTest() {
        Mockito.when(propiedadService.getPropiedadDetail(Mockito.anyLong())).thenReturn(new PropiedadDetailDTO());

        ResponseEntity<PropiedadDetailDTO> actualResult = propiedadController.getPropiedadDetail(Mockito.anyLong());
        assertThat(actualResult.getBody()).usingRecursiveComparison()
                .isEqualTo(new PropiedadDetailDTO());
    }

    @Test
    public void getPropertyDetailThrowsRuntimeExceptionTest() {
        Mockito.when(propiedadService.getPropiedadDetail(Mockito.anyLong())).thenThrow(new RuntimeException());

        ResponseEntity<PropiedadDetailDTO> actualResult = propiedadController.getPropiedadDetail(1L);

        assertEquals(HttpStatus.NOT_FOUND, actualResult.getStatusCode());
        assertThat(actualResult.getBody()).isNull();
    }

    @Test
    public void searchPlacesReturnsMatchingResultsTest() {
        List<PropiedadListDTO> propiedades = new ArrayList<>();
        propiedades.add(new PropiedadListDTO(1L, "Depto en Palermo", "Palermo, Buenos Aires", 100.0, 2, 4.5, "/img1.jpg", "USD"));
        propiedades.add(new PropiedadListDTO(2L, "Loft en Palermo", "Palermo Soho", 150.0, 3, 4.8, "/img2.jpg", "USD"));
        propiedades.add(new PropiedadListDTO(3L, "Casa en Palermo", "Palermo Hollywood", 200.0, 4, 4.7, "/img3.jpg", "USD"));
        propiedades.add(new PropiedadListDTO(4L, "Depto Palermo Chico", "Palermo Chico", 250.0, 2, 4.6, "/img4.jpg", "USD"));
        propiedades.add(new PropiedadListDTO(5L, "Depto en Buenos Aires", "Buenos Aires", 300.0, 5, 4.3, "/img5.jpg", "USD"));
        propiedades.add(new PropiedadListDTO(6L, "Casa Palermo Italia", "Palermo, Italia", 120.0, 2, 4.4, "/img6.jpg", "EUR")); // Queda fuera por limit 5

        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null, null))
                .thenReturn(propiedades);

        ResponseEntity<List<String>> response = propiedadController.searchPlaces("palermo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());

        for (String place : response.getBody()) {
            assertThat(place.toLowerCase()).contains("palermo");
        }
    }

    @Test
    public void searchPlacesEmptyQueryReturnsEmptyList() {
        ResponseEntity<List<String>> response = propiedadController.searchPlaces("");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void searchPlacesServiceThrowsExceptionReturns500() {
        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null, null))
                .thenThrow(new RuntimeException());

        ResponseEntity<List<String>> response = propiedadController.searchPlaces("palermo");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertThat(response.getBody()).isNull();
    }


}
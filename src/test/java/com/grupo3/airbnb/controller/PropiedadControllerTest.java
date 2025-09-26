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
        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null)).thenReturn(new ArrayList<PropiedadListDTO>());

        // Cambiar para incluir los parámetros nuevos
        ResponseEntity<List<PropiedadListDTO>> actualResult = propiedadController.getAllPropiedades(null, null, null, null, null);
        assertEquals(new ArrayList<>(), actualResult.getBody());
    }

    @Test
    public void getAllPropertiesThrowsExceptionTest() {
        // Cambiar para incluir los parámetros nuevos
        Mockito.when(propiedadService.getAllPropiedades(null, null, null, null, null)).thenThrow(new RuntimeException());

        // Cambiar para incluir los parámetros nuevos
        ResponseEntity<List<PropiedadListDTO>> actualResult = propiedadController.getAllPropiedades(null, null, null, null, null);

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
}
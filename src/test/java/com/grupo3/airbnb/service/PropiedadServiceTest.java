package com.grupo3.airbnb.service;

import com.grupo3.airbnb.dto.PropiedadDetailDTO;
import com.grupo3.airbnb.dto.PropiedadListDTO;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IPropiedadImagenRepository;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PropiedadServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    public IPropiedadRepository propiedadRepository;
    @Mock
    public IPropiedadImagenRepository propiedadImagenRepository;
    @InjectMocks
    public PropiedadService propiedadService;

    private Optional<Propiedad> propertyMock;
    private Optional<PropiedadImagen> propertyImageMock;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        propertyMock = mockProperty();
        propertyImageMock = mockPropertyImage(propertyMock.get());
    }


    @Test
    public void testGetAllPropertiesIsOk() {
        Mockito.when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propertyMock.get()));

        List<PropiedadListDTO> expectedList = propiedadService.getAllPropiedades();

        assertThat(expectedList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new PropiedadListDTO(1L, "Titulo", "Ubicacion", 1000d, 2, 6d, "URL_MAIN"));
    }


    @Test
    public void testGetPropertyDetailIsOk() {
        Mockito.when(propiedadRepository.findById(Mockito.anyLong())).thenReturn(propertyMock);
        Mockito.when(propiedadImagenRepository.findByPropiedadId(Mockito.anyLong())).thenReturn(Arrays.asList(propertyImageMock.get()));

        PropiedadDetailDTO expectedPropertyDetail = propiedadService.getPropiedadDetail(Mockito.anyLong());

        assertThat(new PropiedadDetailDTO(1L, "Titulo", "Descripcion", "Ubicacion", 1000d, 2, 2, 1, 6d, Arrays.asList("URL_MAIN")))
                .usingRecursiveComparison()
                .isEqualTo(
                        expectedPropertyDetail);
    }

    @Test
    public void testGetPropertyDetailException() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Propiedad no encontrada");
        Mockito.when(propiedadRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        propiedadService.getPropiedadDetail(Mockito.anyLong());
    }


    private static Optional<Propiedad> mockProperty() {
        Optional<Propiedad> propertyMock = Optional.of(new Propiedad());
        propertyMock.get().setId(1l);
        propertyMock.get().setTitulo("Titulo");
        propertyMock.get().setDescripcion("Descripcion");
        propertyMock.get().setUbicacion("Ubicacion");
        propertyMock.get().setPrecioPorNoche(1000d);
        propertyMock.get().setHuespedes(2);
        propertyMock.get().setHabitaciones(2);
        propertyMock.get().setBanos(1); //BAÑOS
        propertyMock.get().setCalificacion(6d);
        propertyMock.get().setImages(Arrays.asList(mockPropertyImage(propertyMock.get()).get()));

        return propertyMock;
    }

    private static Optional<PropiedadImagen> mockPropertyImage(Propiedad property) {
        Optional<PropiedadImagen> propertyImageMock = Optional.of(new PropiedadImagen());
        propertyImageMock.get().setId(1l);
        propertyImageMock.get().setUrl("URL_MAIN");
        propertyImageMock.get().setPropiedad(property);
        return propertyImageMock;
    }


}

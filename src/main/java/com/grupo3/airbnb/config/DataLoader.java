package com.grupo3.airbnb.config;

import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.repository.IAnfitrionRepository;
import com.grupo3.airbnb.repository.IPropiedadImagenRepository;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    @Autowired
    private IPropiedadImagenRepository imagenRepository;

    @Autowired
    private IAnfitrionRepository anfitrionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (propiedadRepository.count() == 0) {

            // CREAR ANFITRIONES DE EJEMPLO
            Anfitrion anfitrion1 = new Anfitrion();
            anfitrion1.setDni(12345678L);
            anfitrion1.setNombre("María");
            anfitrion1.setApellido("Gonzalez");
            anfitrionRepository.save(anfitrion1);

            Anfitrion anfitrion2 = new Anfitrion();
            anfitrion2.setDni(87654321L);
            anfitrion2.setNombre("Carlos");
            anfitrion2.setApellido("Oliva");
            anfitrionRepository.save(anfitrion2);

            Anfitrion anfitrion3 = new Anfitrion();
            anfitrion3.setDni(11223344L);
            anfitrion3.setNombre("Ana Martínez");
            anfitrion3.setApellido("Martínez");
            anfitrionRepository.save(anfitrion3);

            // Propiedad 1
            Propiedad prop1 = new Propiedad();
            prop1.setTitulo("Departamento en Palermo");
            prop1.setDescripcion("Hermoso depto con vista a la ciudad");
            prop1.setUbicacion("Palermo, Buenos Aires");
            prop1.setPrecioPorNoche(45.0);
            prop1.setHuespedes(4);
            prop1.setHabitaciones(2);
            prop1.setBanos(1);
            prop1.setAnfitrion(anfitrion1);

            // Guardar propiedad primero
            propiedadRepository.save(prop1);

            // Crear y asociar imagen
            PropiedadImagen imgBase = new PropiedadImagen();
            imgBase.setUrl("/images/placeholder.jpg");
            imgBase.setPropiedad(prop1);
            imagenRepository.save(imgBase);

            prop1.getImages().add(imgBase);
            propiedadRepository.save(prop1);

            // Propiedad 2
            Propiedad p1 = new Propiedad();
            p1.setTitulo("Hermoso departamento en el centro");
            p1.setDescripcion("Departamento moderno, cerca de todo.");
            p1.setUbicacion("Buenos Aires");
            p1.setPrecioPorNoche(50.0);
            p1.setHuespedes(2);
            p1.setHabitaciones(1);
            p1.setBanos(1);
            p1.setAnfitrion(anfitrion2);
            propiedadRepository.save(p1);

            PropiedadImagen img1 = new PropiedadImagen();
            img1.setUrl("/images/propiedad1.jpeg");
            img1.setPropiedad(p1);
            imagenRepository.save(img1);

            p1.getImages().add(img1);
            propiedadRepository.save(p1);

            // Propiedad 3
            Propiedad p2 = new Propiedad();
            p2.setTitulo("Cabaña acogedora en la montaña");
            p2.setDescripcion("Cabaña con chimenea y vistas increíbles.");
            p2.setUbicacion("Bariloche");
            p2.setPrecioPorNoche(80.0);
            p2.setHuespedes(4);
            p2.setHabitaciones(2);
            p2.setBanos(1);
            p2.setAnfitrion(anfitrion3);
            propiedadRepository.save(p2);

            PropiedadImagen img2 = new PropiedadImagen();
            img2.setUrl("/images/propiedad2.avif");
            img2.setPropiedad(p2);
            imagenRepository.save(img2);

            p2.getImages().add(img2);
            propiedadRepository.save(p2);

            // Propiedad 4
            Propiedad p3 = new Propiedad();
            p3.setTitulo("Apartamento moderno con vista a la Sagrada Familia");
            p3.setDescripcion("Luminoso y elegante apartamento en el corazón de Barcelona, ideal para turistas.");
            p3.setUbicacion("Barcelona, España");
            p3.setPrecioPorNoche(120.0);
            p3.setHuespedes(3);
            p3.setHabitaciones(1);
            p3.setBanos(1);
            p3.setAnfitrion(anfitrion1);
            propiedadRepository.save(p3);

            PropiedadImagen img3 = new PropiedadImagen();
            img3.setUrl("/images/propiedad3.avif");
            img3.setPropiedad(p3);
            imagenRepository.save(img3);

            p3.getImages().add(img3);
            propiedadRepository.save(p3);

            System.out.println("Datos de ejemplo cargados correctamente");
        }
    }
}
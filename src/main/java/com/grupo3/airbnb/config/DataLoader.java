package com.grupo3.airbnb.config;

import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.Propiedad;
import com.grupo3.airbnb.entity.PropiedadImagen;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.entity.Review;
import com.grupo3.airbnb.repository.IAnfitrionRepository;
import com.grupo3.airbnb.repository.IPropiedadImagenRepository;
import com.grupo3.airbnb.repository.IPropiedadRepository;
import com.grupo3.airbnb.repository.IReservaRepository;
import com.grupo3.airbnb.repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IPropiedadRepository propiedadRepository;

    @Autowired
    private IPropiedadImagenRepository imagenRepository;

    @Autowired
    private IAnfitrionRepository anfitrionRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IReviewRepository reviewRepository;

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

            // ========================================
            // DATOS DE PRUEBA PARA HISTORIA 3
            // ========================================

            System.out.println("🔄 Cargando reservas de prueba...");

            // RESERVA 1: PASADA SIN RESEÑA (debe aparecer en "Sin reseñar")
            Reserva reserva1 = new Reserva();
            reserva1.setPropiedad(prop1);
            reserva1.setHuesped("maria_lopez");
            reserva1.setNroHuespedes(2);
            // Fechas en el PASADO (enero 2025)
            reserva1.setEntrada(Timestamp.valueOf("2025-01-01 14:00:00"));
            reserva1.setSalida(Timestamp.valueOf("2025-01-05 11:00:00"));
            reserva1.setPrecioTotal(180.0);
            reservaRepository.save(reserva1);
            System.out.println("✅ Reserva 1 (PASADA sin reseña): maria_lopez - 01/01 al 05/01");

            // RESERVA 2: FUTURA (NO debe aparecer en "Sin reseñar")
            Reserva reserva2 = new Reserva();
            reserva2.setPropiedad(p1);
            reserva2.setHuesped("maria_lopez");
            reserva2.setNroHuespedes(3);
            // Fechas en el FUTURO (diciembre 2025)
            reserva2.setEntrada(Timestamp.valueOf("2025-12-10 14:00:00"));
            reserva2.setSalida(Timestamp.valueOf("2025-12-15 11:00:00"));
            reserva2.setPrecioTotal(250.0);
            reservaRepository.save(reserva2);
            System.out.println("✅ Reserva 2 (FUTURA): maria_lopez - 10/12 al 15/12");

            // RESERVA 3: PASADA CON RESEÑA (NO debe aparecer en "Sin reseñar")
            Reserva reserva3 = new Reserva();
            reserva3.setPropiedad(p2);
            reserva3.setHuesped("juan_perez");
            reserva3.setNroHuespedes(4);
            // Fechas en el PASADO (diciembre 2024)
            reserva3.setEntrada(Timestamp.valueOf("2024-12-20 14:00:00"));
            reserva3.setSalida(Timestamp.valueOf("2024-12-25 11:00:00"));
            reserva3.setPrecioTotal(400.0);
            reservaRepository.save(reserva3);
            System.out.println("✅ Reserva 3 (PASADA): juan_perez - 20/12/2024 al 25/12/2024");

            // RESEÑA para la Reserva 3 (para que NO aparezca en el filtro)
            Review review1 = new Review();
            review1.setReserva(reserva3);
            review1.setPropiedad(p2);
            review1.setUsuario("juan_perez");
            review1.setComentario("Excelente cabaña, perfecta para descansar. La chimenea es hermosa.");
            review1.setCalificacionGeneral(5.0);
            review1.setCalificacionLimpieza(5.0);
            review1.setCalificacionUbicacion(4.5);
            review1.setCalificacionComunicacion(5.0);
            review1.setPublicada(true);
            review1.setFechaPublicacion(LocalDateTime.now());
            reviewRepository.save(review1);
            System.out.println("✅ Reseña creada para juan_perez (reserva 3)");

            // RESERVA 4: Otra PASADA SIN RESEÑA para el mismo usuario (para probar múltiples)
            Reserva reserva4 = new Reserva();
            reserva4.setPropiedad(p3);
            reserva4.setHuesped("maria_lopez");
            reserva4.setNroHuespedes(2);
            // Fechas en el PASADO (octubre 2024)
            reserva4.setEntrada(Timestamp.valueOf("2024-10-15 14:00:00"));
            reserva4.setSalida(Timestamp.valueOf("2024-10-20 11:00:00"));
            reserva4.setPrecioTotal(600.0);
            reservaRepository.save(reserva4);
            System.out.println("✅ Reserva 4 (PASADA sin reseña): maria_lopez - 15/10/2024 al 20/10/2024");

            System.out.println("\n🎉 Datos de ejemplo cargados correctamente");
            System.out.println("📋 RESUMEN PARA PROBAR HISTORIA 3:");
            System.out.println("   Usuario: maria_lopez");
            System.out.println("   - 2 reservas PASADAS sin reseña (deben aparecer en 'Sin reseñar')");
            System.out.println("   - 1 reserva FUTURA (NO debe aparecer en 'Sin reseñar')");
            System.out.println("   Usuario: juan_perez");
            System.out.println("   - 1 reserva PASADA con reseña (NO debe aparecer en 'Sin reseñar')");
        }
    }
}
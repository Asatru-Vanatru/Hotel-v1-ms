// Paquete raíz del microservicio ms-habitaciones
package com.hotel.habitaciones;

// Importa la clase de arranque de Spring Boot
import org.springframework.boot.SpringApplication;
// Importa la anotación central de configuración y arranque de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ═══════════════════════════════════════════════════════════
// HabitacionesApplication.java
// Punto de entrada del microservicio ms-habitaciones.
// Gestiona el catálogo de habitaciones del hotel.
// Puerto: 8082 | Base de datos: db_hotel_habitaciones
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: activa autoconfiguración, escaneo de componentes y configuración
@SpringBootApplication
public class HabitacionesApplication {

    // main: inicia la JVM y arranca el contexto de Spring Boot para este microservicio
    public static void main(String[] args) {
        // Levanta el servidor Tomcat en puerto 8082 y conecta a db_hotel_habitaciones
        SpringApplication.run(HabitacionesApplication.class, args);
    }

}

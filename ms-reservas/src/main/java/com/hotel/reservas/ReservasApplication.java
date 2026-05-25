// Paquete raíz del microservicio ms-reservas
package com.hotel.reservas;

// Importa la clase principal que inicia la aplicación Spring Boot
import org.springframework.boot.SpringApplication;
// Importa la anotación que activa todas las funciones automáticas de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ═══════════════════════════════════════════════════════════
// ReservasApplication.java
// Punto de entrada del microservicio ms-reservas.
// Gestiona las reservas de habitaciones en el hotel.
// Puerto: 8083 | Base de datos: db_hotel_reservas
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: configura automáticamente el contexto de Spring Boot
@SpringBootApplication
public class ReservasApplication {

    // main: método de inicio que arranca el servidor Tomcat embebido
    public static void main(String[] args) {
        // Inicia el contexto Spring, conecta a MySQL en puerto 3306 y expone la API en 8083
        SpringApplication.run(ReservasApplication.class, args);
    }

}

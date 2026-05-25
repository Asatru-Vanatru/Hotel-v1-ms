// Paquete raíz del microservicio ms-mantenimiento
package com.hotel.mantenimiento;

import org.springframework.boot.SpringApplication; // Clase de inicio del contexto Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Activa la autoconfiguración completa

// ═══════════════════════════════════════════════════════════
// MantenimientoApplication.java
// Punto de entrada del microservicio ms-mantenimiento.
// Gestiona solicitudes de mantenimiento y reparación de habitaciones.
// Puerto: 8090 | Base de datos: db_hotel_mantenimiento
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: activa todo el motor de Spring Boot para este microservicio
@SpringBootApplication
public class MantenimientoApplication {

    // main: método de entrada de la JVM para iniciar el microservicio de mantenimiento
    public static void main(String[] args) {
        // Levanta Tomcat en 8090 y conecta a la base de datos de mantenimiento
        SpringApplication.run(MantenimientoApplication.class, args);
    }

}

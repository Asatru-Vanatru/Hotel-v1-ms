// Paquete raíz del microservicio ms-servicios
package com.hotel.servicios;

import org.springframework.boot.SpringApplication; // Clase de arranque de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotación de configuración automática

// ═══════════════════════════════════════════════════════════
// ServiciosApplication.java
// Punto de entrada del microservicio ms-servicios.
// Administra el catálogo de servicios adicionales del hotel.
// Puerto: 8086 | Base de datos: db_hotel_servicios
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: habilita la autoconfiguración y el escaneo de componentes
@SpringBootApplication
public class ServiciosApplication {

    // main: método de inicio para arrancar el microservicio de servicios adicionales
    public static void main(String[] args) {
        // Inicia Tomcat en 8086 y conecta a la base de datos del catálogo de servicios
        SpringApplication.run(ServiciosApplication.class, args);
    }

}

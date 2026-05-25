// Paquete raíz del microservicio ms-checkin
package com.hotel.checkin;

import org.springframework.boot.SpringApplication; // Clase principal de arranque Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotación de configuración automática

// ═══════════════════════════════════════════════════════════
// CheckinApplication.java
// Punto de entrada del microservicio ms-checkin.
// Registra la entrada de huéspedes al hotel.
// Puerto: 8084 | Base de datos: db_hotel_checkin
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: activa el escaneo de componentes y la autoconfiguración de Spring
@SpringBootApplication
public class CheckinApplication {

    // main: método de entrada de la JVM para iniciar el microservicio de check-in
    public static void main(String[] args) {
        // Inicia el contexto Spring, arranca Tomcat en 8084 y conecta a db_hotel_checkin
        SpringApplication.run(CheckinApplication.class, args);
    }

}

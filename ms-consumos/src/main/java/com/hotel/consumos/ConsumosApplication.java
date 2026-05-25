// Paquete raíz del microservicio ms-consumos
package com.hotel.consumos;

import org.springframework.boot.SpringApplication; // Clase de arranque de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotación de configuración automática

// ═══════════════════════════════════════════════════════════
// ConsumosApplication.java
// Punto de entrada del microservicio ms-consumos.
// Registra los servicios adicionales consumidos por reserva.
// Puerto: 8087 | Base de datos: db_hotel_consumos
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: activa la autoconfiguración y el escaneo de beans de Spring
@SpringBootApplication
public class ConsumosApplication {

    // main: inicia el contexto Spring y el servidor embebido del microservicio de consumos
    public static void main(String[] args) {
        // Levanta Tomcat en puerto 8087 y conecta a db_hotel_consumos
        SpringApplication.run(ConsumosApplication.class, args);
    }

}

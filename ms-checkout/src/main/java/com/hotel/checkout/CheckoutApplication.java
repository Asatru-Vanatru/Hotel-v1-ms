// Paquete raíz del microservicio ms-checkout
package com.hotel.checkout;

import org.springframework.boot.SpringApplication; // Clase de inicio de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Habilita autoconfiguración

// ═══════════════════════════════════════════════════════════
// CheckoutApplication.java
// Punto de entrada del microservicio ms-checkout.
// Gestiona la salida de huéspedes y el cierre de estadías.
// Puerto: 8085 | Base de datos: db_hotel_checkout
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: configura y arranca automáticamente el microservicio Spring Boot
@SpringBootApplication
public class CheckoutApplication {

    // main: inicia la JVM y el contexto de Spring para el microservicio de check-out
    public static void main(String[] args) {
        // Levanta el servidor en puerto 8085 y conecta a la base de datos de check-out
        SpringApplication.run(CheckoutApplication.class, args);
    }

}

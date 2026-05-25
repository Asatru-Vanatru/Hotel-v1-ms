// Paquete raíz del microservicio ms-pagos
package com.hotel.pagos;

import org.springframework.boot.SpringApplication; // Clase principal de arranque Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Habilita la autoconfiguración

// ═══════════════════════════════════════════════════════════
// PagosApplication.java
// Punto de entrada del microservicio ms-pagos.
// Procesa y registra los pagos de reservas del hotel.
// Puerto: 8088 | Base de datos: db_hotel_pagos
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: configura automáticamente Spring Boot y escanea componentes
@SpringBootApplication
public class PagosApplication {

    // main: método de inicio que levanta el microservicio de pagos
    public static void main(String[] args) {
        // Inicia el servidor Tomcat en 8088 y establece la conexión con db_hotel_pagos
        SpringApplication.run(PagosApplication.class, args);
    }

}

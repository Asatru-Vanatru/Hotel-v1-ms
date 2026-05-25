// Paquete raíz del microservicio ms-empleados
package com.hotel.empleados;

import org.springframework.boot.SpringApplication; // Clase de arranque de la aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotación de autoconfiguración

// ═══════════════════════════════════════════════════════════
// EmpleadosApplication.java
// Punto de entrada del microservicio ms-empleados.
// Administra el personal del hotel por departamento y cargo.
// Puerto: 8089 | Base de datos: db_hotel_empleados
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: habilita autoconfiguración, escaneo de componentes y configuración de beans
@SpringBootApplication
public class EmpleadosApplication {

    // main: punto de inicio de la JVM para el microservicio de gestión de personal
    public static void main(String[] args) {
        // Arranca el contexto Spring, Tomcat en 8089 y la conexión a db_hotel_empleados
        SpringApplication.run(EmpleadosApplication.class, args);
    }

}

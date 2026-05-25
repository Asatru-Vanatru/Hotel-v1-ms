// Paquete de pruebas del microservicio ms-servicios
package com.hotel.servicios;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa aserciones de JUnit 5

// Clase de pruebas de integración del microservicio ms-servicios
@SpringBootTest // Levanta el contexto completo de Spring Boot para la prueba de integración
class ServiciosApplicationTests {

    @Test // Prueba que el contexto de ms-servicios se inicia sin errores de configuración
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-servicios cargó correctamente"); // Inicio exitoso
    }

}

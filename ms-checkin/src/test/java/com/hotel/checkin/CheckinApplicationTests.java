// Paquete de pruebas del microservicio ms-checkin
package com.hotel.checkin;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba ejecutable
import static org.junit.jupiter.api.Assertions.*; // Métodos de aserción de JUnit 5

// Clase de pruebas de integración del microservicio ms-checkin
@SpringBootTest // Levanta el contexto completo de Spring Boot para la prueba de integración
class CheckinApplicationTests {

    @Test // Prueba unitaria: verifica que el contexto de ms-checkin se levanta sin errores
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-checkin cargó correctamente"); // Aserción exitosa
    }

}

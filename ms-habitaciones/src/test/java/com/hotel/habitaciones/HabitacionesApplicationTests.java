// Paquete de pruebas del microservicio ms-habitaciones
package com.hotel.habitaciones;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto Spring Boot completo
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa métodos de aserción de JUnit 5

// Clase de pruebas de integración del microservicio ms-habitaciones
@SpringBootTest // Levanta el contexto completo de Spring Boot para la prueba
class HabitacionesApplicationTests {

    @Test // Caso de prueba: verifica que el contexto de ms-habitaciones carga correctamente
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-habitaciones cargó correctamente"); // Aserción de carga exitosa
    }

}

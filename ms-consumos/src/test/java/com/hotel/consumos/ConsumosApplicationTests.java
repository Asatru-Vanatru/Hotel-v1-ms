// Paquete de pruebas del microservicio ms-consumos
package com.hotel.consumos;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba en JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa métodos de aserción de JUnit 5

// Clase de pruebas de integración del microservicio ms-consumos
@SpringBootTest // Levanta todo el contexto de Spring Boot para verificar configuración
class ConsumosApplicationTests {

    @Test // Prueba que el contexto de ms-consumos se levanta correctamente
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-consumos cargó correctamente"); // Verifica inicio
    }

}

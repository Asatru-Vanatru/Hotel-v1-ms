// Paquete de pruebas del microservicio ms-pagos
package com.hotel.pagos;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa aserciones de JUnit 5

// Clase de pruebas de integración del microservicio ms-pagos
@SpringBootTest // Levanta el contexto de Spring Boot completo para la prueba
class PagosApplicationTests {

    @Test // Verifica que el contexto de ms-pagos se configura e inicia sin errores
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-pagos cargó correctamente"); // Verificación de inicio
    }

}

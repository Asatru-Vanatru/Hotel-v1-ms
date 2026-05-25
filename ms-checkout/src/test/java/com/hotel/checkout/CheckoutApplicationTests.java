// Paquete de pruebas del microservicio ms-checkout
package com.hotel.checkout;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot completo
import org.junit.jupiter.api.Test; // Anotación JUnit 5 que marca el método como prueba
import static org.junit.jupiter.api.Assertions.*; // Importa aserciones de JUnit 5

// Clase de pruebas de integración del microservicio ms-checkout
@SpringBootTest // Levanta el contexto de Spring Boot para verificar la configuración
class CheckoutApplicationTests {

    @Test // Prueba que el contexto de ms-checkout se configura y levanta correctamente
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-checkout cargó correctamente"); // Verificación de inicio
    }

}

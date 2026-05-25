// Paquete de pruebas del microservicio ms-reservas
package com.hotel.reservas;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto Spring Boot completo
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa métodos de aserción de JUnit 5

// Clase de pruebas de integración del microservicio ms-reservas
@SpringBootTest // Levanta todo el contexto de Spring Boot para verificar la configuración
class ReservasApplicationTests {

    @Test // Prueba que el contexto del microservicio ms-reservas se inicia sin errores
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-reservas cargó correctamente"); // Verifica inicio exitoso
    }

}

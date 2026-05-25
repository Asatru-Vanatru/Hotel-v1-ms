// Paquete de pruebas del microservicio ms-mantenimiento
package com.hotel.mantenimiento;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Anotación JUnit 5 para marcar métodos de prueba
import static org.junit.jupiter.api.Assertions.*; // Importa aserciones de JUnit 5

// Clase de pruebas de integración del microservicio ms-mantenimiento
@SpringBootTest // Levanta el contexto completo de Spring Boot para la prueba
class MantenimientoApplicationTests {

    @Test // Verifica que el contexto de ms-mantenimiento se configura e inicia sin errores
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-mantenimiento cargó correctamente"); // Verificación
    }

}

// Paquete de pruebas del microservicio ms-clientes
package com.hotel.clientes;

// Importa la anotación principal para pruebas de integración de Spring Boot
import org.springframework.boot.test.context.SpringBootTest;
// Importa las clases de JUnit 5 para escribir y ejecutar pruebas unitarias
import org.junit.jupiter.api.Test;
// Importa la clase de aserciones de JUnit 5 para verificar condiciones en los tests
import static org.junit.jupiter.api.Assertions.*;

// ═══════════════════════════════════════════════════════════
// ClientesApplicationTests.java
// Clase de pruebas de integración del microservicio ms-clientes.
//
// @SpringBootTest: levanta el contexto completo de Spring Boot
// para verificar que todos los beans se configuran correctamente.
//
// Herramienta usada: JUnit 5 (incluido en spring-boot-starter-test)
// ═══════════════════════════════════════════════════════════

// @SpringBootTest: carga el contexto de aplicación completo para pruebas de integración
@SpringBootTest
class ClientesApplicationTests {

    // @Test: anotación de JUnit 5 que marca este método como caso de prueba ejecutable
    @Test
    // contextLoads: verifica que el contexto de Spring Boot se inicia sin errores
    // Si falla, hay un problema de configuración en el microservicio
    void contextLoads() {
        // Si llega hasta aquí sin lanzar excepción, el contexto cargó correctamente
        assertTrue(true, "El contexto de Spring Boot del microservicio ms-clientes cargó correctamente"); // Verifica carga exitosa
    }

}

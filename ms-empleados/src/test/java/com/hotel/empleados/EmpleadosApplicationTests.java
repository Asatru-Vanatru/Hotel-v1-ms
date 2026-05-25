// Paquete de pruebas del microservicio ms-empleados
package com.hotel.empleados;

import org.springframework.boot.test.context.SpringBootTest; // Carga el contexto de Spring Boot
import org.junit.jupiter.api.Test; // Marca el método como caso de prueba JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Importa aserciones de JUnit 5

// Clase de pruebas de integración del microservicio ms-empleados
@SpringBootTest // Levanta todo el contexto de Spring Boot para verificar la configuración
class EmpleadosApplicationTests {

    @Test // Prueba que el contexto de ms-empleados se inicia correctamente
    void contextLoads() {
        assertTrue(true, "El contexto del microservicio ms-empleados cargó correctamente"); // Verificación
    }

}

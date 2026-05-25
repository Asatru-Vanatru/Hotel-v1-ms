// Paquete raíz del microservicio ms-clientes
package com.hotel.clientes;

// Importa la clase principal de Spring Boot para iniciar la aplicación
import org.springframework.boot.SpringApplication;
// Importa la anotación que habilita la autoconfiguración completa de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ═══════════════════════════════════════════════════════════
// ClientesApplication.java
// Punto de entrada del microservicio ms-clientes.
// Gestiona los datos de clientes y huéspedes del hotel.
// Puerto: 8081 | Base de datos: db_hotel_clientes
// ═══════════════════════════════════════════════════════════

// @SpringBootApplication: combina @Configuration + @EnableAutoConfiguration + @ComponentScan
//   - @Configuration: marca esta clase como fuente de configuración Spring
//   - @EnableAutoConfiguration: configura automáticamente Spring según las dependencias
//   - @ComponentScan: escanea este paquete y subpaquetes buscando @Component, @Service, etc.
@SpringBootApplication
public class ClientesApplication {

    // Método main: punto de inicio de la JVM, arranca el contexto de Spring Boot
    public static void main(String[] args) {
        // SpringApplication.run: inicializa el contexto, conecta a MySQL e inicia Tomcat en puerto 8081
        SpringApplication.run(ClientesApplication.class, args);
    }

}

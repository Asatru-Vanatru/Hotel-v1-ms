// Paquete de DTOs del microservicio ms-clientes
package com.hotel.clientes.dto;

// Importa las anotaciones de Lombok para generación automática de código
import lombok.AllArgsConstructor; // Genera constructor con todos los parámetros
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Genera constructor vacío para Jackson y otros frameworks

// ═══════════════════════════════════════════════════════════
// ClienteResponseDTO.java
//
// DTO de SALIDA: lo que el servidor devuelve al cliente
// después de un GET, POST o PUT exitoso.
//
// Sin validaciones (@NotBlank, @Email, etc.) porque este
// DTO es construido por el servidor, no viene del cliente.
// Incluye el campo 'id' generado por MySQL.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos estándar de un POJO automáticamente
@NoArgsConstructor // Constructor vacío necesario para que Jackson serialice a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en Service
public class ClienteResponseDTO {

    private Long id; // ID único generado por MySQL, incluido en la respuesta para identificar el recurso
    private String nombre; // Nombre del cliente devuelto en la respuesta
    private String apellido; // Apellido del cliente devuelto en la respuesta
    private String email; // Email del cliente devuelto en la respuesta
    private String telefono; // Teléfono del cliente devuelto en la respuesta
    private String tipoDocumento; // Tipo de documento del cliente en la respuesta
    private String numeroDocumento; // Número de documento del cliente en la respuesta
    private String nacionalidad; // Nacionalidad del cliente en la respuesta

}

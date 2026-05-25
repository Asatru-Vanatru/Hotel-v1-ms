// Paquete de DTOs del microservicio ms-servicios
package com.hotel.servicios.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad automáticamente
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo preciso para el precio del servicio en la respuesta

// DTO de SALIDA: datos del servicio adicional devueltos en la respuesta HTTP
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en el Service
public class ServicioResponseDTO {

    private Long id; // ID único del servicio generado por MySQL
    private String nombre; // Nombre del servicio en la respuesta
    private String descripcion; // Descripción del servicio en la respuesta
    private BigDecimal precio; // Precio unitario del servicio en la respuesta
    private String categoria; // Categoría del servicio en la respuesta
    private Boolean disponible; // Estado de disponibilidad del servicio en la respuesta

}

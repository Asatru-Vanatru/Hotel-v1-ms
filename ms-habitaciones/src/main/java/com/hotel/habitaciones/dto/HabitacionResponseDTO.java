// Paquete de DTOs del microservicio ms-habitaciones
package com.hotel.habitaciones.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros para construir la respuesta
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Constructor vacío necesario para serialización Jackson a JSON
import java.math.BigDecimal; // Tipo preciso para el precio por noche en la respuesta

// ═══════════════════════════════════════════════════════════
// HabitacionResponseDTO.java
// DTO de SALIDA: datos de habitación enviados al cliente.
// Sin validaciones, construido por el servidor.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos estándar del POJO de respuesta
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor completo para construir la respuesta en la capa de servicio
public class HabitacionResponseDTO {

    private Long id; // ID único de la habitación generado por MySQL
    private String numero; // Número visible de la habitación en la respuesta
    private String tipo; // Tipo de habitación en la respuesta (SIMPLE, DOBLE, SUITE...)
    private Integer capacidad; // Capacidad máxima de la habitación en la respuesta
    private BigDecimal precioNoche; // Precio por noche devuelto en la respuesta
    private String descripcion; // Descripción de la habitación en la respuesta
    private String estado; // Estado actual de la habitación en la respuesta
    private Integer piso; // Número de piso de la habitación en la respuesta

}

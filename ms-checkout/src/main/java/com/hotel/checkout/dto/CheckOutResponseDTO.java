// Paquete de DTOs del microservicio ms-checkout
package com.hotel.checkout.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros para construir respuesta
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo exacto para el total final en la respuesta
import java.time.LocalDateTime; // Tipo para la fecha y hora del check-out en la respuesta

// DTO de SALIDA: datos del check-out devueltos al cliente en la respuesta HTTP
@Data // Genera todos los métodos estándar del POJO de respuesta
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir en el Service
public class CheckOutResponseDTO {

    private Long id; // ID único del check-out generado por MySQL
    private Long reservaId; // ID de la reserva cerrada con este check-out
    private Long clienteId; // ID del cliente que realizó el check-out
    private Long habitacionId; // ID de la habitación liberada
    private LocalDateTime fechaHoraCheckOut; // Momento exacto del check-out
    private BigDecimal totalFinal; // Total cobrado al huésped al finalizar su estadía
    private Integer diasEstancia; // Número de noches de la estadía

}

// Paquete de DTOs del microservicio ms-reservas
package com.hotel.reservas.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros para construir respuesta
import lombok.Data; // Genera getters, setters y métodos de utilidad
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo preciso para el total estimado en la respuesta
import java.time.LocalDate; // Tipo para las fechas de la reserva en la respuesta

// ═══════════════════════════════════════════════════════════
// ReservaResponseDTO.java
// DTO de SALIDA: datos de reserva enviados al cliente.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos estándar del POJO
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor completo para construir la respuesta en el Service
public class ReservaResponseDTO {

    private Long id; // ID único de la reserva generado por MySQL
    private Long clienteId; // ID del cliente asociado a la reserva
    private Long habitacionId; // ID de la habitación reservada
    private LocalDate fechaIngreso; // Fecha de llegada del huésped
    private LocalDate fechaSalida; // Fecha de salida del huésped
    private String estado; // Estado actual de la reserva
    private BigDecimal totalEstimado; // Monto total estimado de la reserva
    private Integer numeroPasajeros; // Cantidad de huéspedes de la reserva

}

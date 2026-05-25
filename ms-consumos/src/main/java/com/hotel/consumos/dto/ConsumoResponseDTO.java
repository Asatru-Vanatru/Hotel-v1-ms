// Paquete de DTOs del microservicio ms-consumos
package com.hotel.consumos.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera todos los métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo preciso para el subtotal en la respuesta
import java.time.LocalDateTime; // Tipo para la fecha y hora del consumo en la respuesta

// DTO de SALIDA: datos del consumo devueltos en la respuesta HTTP
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en el Service
public class ConsumoResponseDTO {

    private Long id; // ID único del consumo generado por MySQL
    private Long reservaId; // ID de la reserva asociada al consumo
    private Long clienteId; // ID del cliente que realizó el consumo
    private Long servicioId; // ID del servicio adicional consumido
    private Integer cantidad; // Número de unidades del servicio consumidas
    private LocalDateTime fechaConsumo; // Momento exacto en que se realizó el consumo
    private BigDecimal subtotal; // Monto calculado del consumo (precio × cantidad)
    private String observaciones; // Notas adicionales del consumo registradas

}

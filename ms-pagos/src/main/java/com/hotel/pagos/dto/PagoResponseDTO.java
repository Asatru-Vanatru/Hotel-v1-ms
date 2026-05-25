// Paquete de DTOs del microservicio ms-pagos
package com.hotel.pagos.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo preciso para el monto en la respuesta
import java.time.LocalDateTime; // Tipo para la fecha y hora del pago en la respuesta

// DTO de SALIDA: datos del pago devueltos al cliente en la respuesta HTTP
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en el Service
public class PagoResponseDTO {

    private Long id; // ID único del pago generado por MySQL
    private Long reservaId; // ID de la reserva asociada al pago
    private Long clienteId; // ID del cliente que realizó el pago
    private BigDecimal monto; // Monto pagado en la transacción
    private String metodoPago; // Método de pago utilizado
    private String estado; // Estado actual del pago
    private LocalDateTime fechaPago; // Momento en que se realizó el pago
    private String referencia; // Número de comprobante o referencia del pago

}

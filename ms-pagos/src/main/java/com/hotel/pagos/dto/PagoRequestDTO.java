// Paquete de DTOs del microservicio ms-pagos
package com.hotel.pagos.dto;

import jakarta.validation.constraints.DecimalMin; // Valida que el monto sea mayor a 0
import jakarta.validation.constraints.Min; // Valida que los IDs sean positivos
import jakarta.validation.constraints.NotBlank; // Valida que los Strings no estén vacíos
import jakarta.validation.constraints.NotNull; // Valida que los campos no sean null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha no sea futura
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.math.BigDecimal; // Tipo exacto para el monto del pago
import java.time.LocalDateTime; // Tipo para la fecha y hora del pago

// DTO de ENTRADA para registrar una transacción de pago de una reserva del hotel
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserializar el JSON del body
@AllArgsConstructor // Constructor completo para tests y creación directa
public class PagoRequestDTO {

    @NotNull(message = "El ID de la reserva no puede ser nulo") // Reserva obligatoria
    @Min(value = 1, message = "El ID de la reserva debe ser positivo") // ID válido
    private Long reservaId; // ID de la reserva que se está liquidando con este pago

    @NotNull(message = "El ID del cliente no puede ser nulo") // Cliente obligatorio
    @Min(value = 1, message = "El ID del cliente debe ser positivo") // ID válido
    private Long clienteId; // ID del cliente que realiza el pago

    @NotNull(message = "El monto no puede ser nulo") // Monto obligatorio
    @DecimalMin(value = "0.01", message = "El monto del pago debe ser mayor a 0") // Monto positivo
    private BigDecimal monto; // Cantidad de dinero pagada en esta transacción

    @NotBlank(message = "El método de pago no puede estar vacío") // Método obligatorio
    @Size(max = 30, message = "El método de pago no puede superar 30 caracteres") // Límite MySQL
    private String metodoPago; // Forma de pago: EFECTIVO, TARJETA_CREDITO, TRANSFERENCIA, etc.

    @NotBlank(message = "El estado del pago no puede estar vacío") // Estado obligatorio
    @Size(max = 20, message = "El estado no puede superar 20 caracteres") // PENDIENTE, COMPLETADO, etc.
    private String estado; // Estado inicial del pago al registrarse

    @NotNull(message = "La fecha del pago no puede ser nula") // Fecha obligatoria
    @PastOrPresent(message = "La fecha del pago no puede ser en el futuro") // Solo ahora o antes
    private LocalDateTime fechaPago; // Momento exacto en que se realizó la transacción

    @Size(max = 100, message = "La referencia no puede superar 100 caracteres") // Referencia opcional
    private String referencia; // Número de boleta, voucher o comprobante de la transacción

}

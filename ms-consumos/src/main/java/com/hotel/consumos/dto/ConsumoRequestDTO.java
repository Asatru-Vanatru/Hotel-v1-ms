// Paquete de DTOs del microservicio ms-consumos
package com.hotel.consumos.dto;

import jakarta.validation.constraints.DecimalMin; // Valida que el subtotal sea positivo
import jakarta.validation.constraints.Min; // Valida que los IDs y cantidades sean positivos
import jakarta.validation.constraints.NotNull; // Valida que los campos obligatorios no sean null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha no sea futura
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.math.BigDecimal; // Tipo exacto para el subtotal del consumo
import java.time.LocalDateTime; // Tipo para la fecha y hora del consumo

// DTO de ENTRADA para registrar un consumo de servicio adicional durante una reserva
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserialización JSON por Jackson
@AllArgsConstructor // Constructor completo para tests
public class ConsumoRequestDTO {

    @NotNull(message = "El ID de la reserva no puede ser nulo") // Reserva obligatoria
    @Min(value = 1, message = "El ID de la reserva debe ser positivo") // ID válido
    private Long reservaId; // Reserva activa durante la cual se realiza el consumo

    @NotNull(message = "El ID del cliente no puede ser nulo") // Cliente obligatorio
    @Min(value = 1, message = "El ID del cliente debe ser positivo") // ID válido
    private Long clienteId; // Cliente que consume el servicio adicional

    @NotNull(message = "El ID del servicio no puede ser nulo") // Servicio obligatorio
    @Min(value = 1, message = "El ID del servicio debe ser positivo") // ID válido
    private Long servicioId; // Servicio adicional que está siendo consumido

    @NotNull(message = "La cantidad no puede ser nula") // Cantidad obligatoria
    @Min(value = 1, message = "La cantidad debe ser al menos 1") // Al menos 1 unidad consumida
    private Integer cantidad; // Número de unidades del servicio consumidas

    @NotNull(message = "La fecha y hora del consumo no puede ser nula") // Momento obligatorio
    @PastOrPresent(message = "La fecha del consumo no puede ser en el futuro") // Solo presente o pasado
    private LocalDateTime fechaConsumo; // Momento en que se registra el consumo del servicio

    @NotNull(message = "El subtotal no puede ser nulo") // Subtotal obligatorio para el cobro
    @DecimalMin(value = "0.01", message = "El subtotal debe ser mayor a 0") // Subtotal positivo
    private BigDecimal subtotal; // Monto calculado: precio_servicio × cantidad

    @Size(max = 300, message = "Las observaciones no pueden superar 300 caracteres") // Límite opcional
    private String observaciones; // Notas sobre el consumo (alergias, preferencias, etc.)

}

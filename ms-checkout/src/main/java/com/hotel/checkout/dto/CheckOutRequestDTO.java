// Paquete de DTOs del microservicio ms-checkout
package com.hotel.checkout.dto;

import jakarta.validation.constraints.DecimalMin; // Valida que el total sea mayor a 0
import jakarta.validation.constraints.Min; // Valida que el entero sea mayor o igual al mínimo
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha no sea futura
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para deserialización Jackson
import java.math.BigDecimal; // Tipo exacto para el total final del cobro
import java.time.LocalDateTime; // Tipo para la fecha y hora del check-out

// DTO de ENTRADA para registrar el proceso de check-out de un huésped
@Data // Genera getters, setters y métodos de utilidad
@NoArgsConstructor // Constructor vacío requerido para deserializar el JSON del body
@AllArgsConstructor // Constructor completo útil para tests
public class CheckOutRequestDTO {

    @NotNull(message = "El ID de la reserva no puede ser nulo") // Reserva obligatoria
    @Min(value = 1, message = "El ID de la reserva debe ser positivo") // ID válido mayor a 0
    private Long reservaId; // ID de la reserva que se está cerrando con el check-out

    @NotNull(message = "El ID del cliente no puede ser nulo") // Cliente obligatorio
    @Min(value = 1, message = "El ID del cliente debe ser positivo") // ID válido mayor a 0
    private Long clienteId; // ID del cliente que realiza el check-out

    @NotNull(message = "El ID de la habitación no puede ser nulo") // Habitación obligatoria
    @Min(value = 1, message = "El ID de la habitación debe ser positivo") // ID válido mayor a 0
    private Long habitacionId; // ID de la habitación que se está liberando

    @NotNull(message = "La fecha y hora del check-out no puede ser nula") // Momento obligatorio
    @PastOrPresent(message = "La fecha del check-out no puede ser en el futuro") // Solo ahora o antes
    private LocalDateTime fechaHoraCheckOut; // Momento exacto de la salida del huésped

    @NotNull(message = "El total final no puede ser nulo") // El cobro final es obligatorio
    @DecimalMin(value = "0.00", message = "El total final no puede ser negativo") // Total positivo
    private BigDecimal totalFinal; // Monto total cobrado incluyendo habitación y servicios

    @NotNull(message = "Los días de estadía no pueden ser nulos") // Días obligatorios
    @Min(value = 1, message = "La estadía debe ser de al menos 1 día") // Mínimo 1 noche
    private Integer diasEstancia; // Total de noches de la estadía del huésped

}

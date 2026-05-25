// Paquete de DTOs del microservicio ms-checkin
package com.hotel.checkin.dto;

import jakarta.validation.constraints.Min; // Valida que el ID sea un número positivo
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha no sea futura
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.time.LocalDateTime; // Tipo para la fecha y hora del momento del check-in

// DTO de ENTRADA para registrar el proceso de check-in de un huésped
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserializar el JSON del body de la petición
@AllArgsConstructor // Constructor completo para crear instancias en tests
public class CheckInRequestDTO {

    @NotNull(message = "El ID de la reserva no puede ser nulo") // La reserva es obligatoria
    @Min(value = 1, message = "El ID de la reserva debe ser positivo") // ID de reserva válido
    private Long reservaId; // ID de la reserva que está siendo activada con el check-in

    @NotNull(message = "El ID del cliente no puede ser nulo") // El cliente es obligatorio
    @Min(value = 1, message = "El ID del cliente debe ser positivo") // ID de cliente válido
    private Long clienteId; // ID del cliente que realiza el check-in en recepción

    @NotNull(message = "El ID de la habitación no puede ser nulo") // La habitación es obligatoria
    @Min(value = 1, message = "El ID de la habitación debe ser positivo") // ID de habitación válido
    private Long habitacionId; // ID de la habitación que se asigna al huésped

    @NotNull(message = "La fecha y hora del check-in no puede ser nula") // Momento obligatorio
    @PastOrPresent(message = "La fecha del check-in no puede ser en el futuro") // Solo pasado o presente
    private LocalDateTime fechaHoraCheckIn; // Momento exacto en que el huésped hace el check-in

    @Size(max = 400, message = "Las observaciones no pueden superar 400 caracteres") // Límite del texto
    private String observaciones; // Notas del recepcionista (estado de la habitación, peticiones, etc.)

}

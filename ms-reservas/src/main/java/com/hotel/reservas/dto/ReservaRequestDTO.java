// Paquete de DTOs del microservicio ms-reservas
package com.hotel.reservas.dto;

import jakarta.validation.constraints.Future; // Valida que la fecha sea en el futuro
import jakarta.validation.constraints.Min; // Valida que el entero sea mayor o igual al mínimo
import jakarta.validation.constraints.NotBlank; // Valida que el String no esté vacío
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea null
import jakarta.validation.constraints.Size; // Valida longitud máxima del String
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.time.LocalDate; // Tipo para fechas de ingreso y salida sin hora

// ═══════════════════════════════════════════════════════════
// ReservaRequestDTO.java
// DTO de ENTRADA para crear o actualizar una reserva.
// Incluye todas las validaciones de negocio.
// ═══════════════════════════════════════════════════════════

@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserialización del JSON del body
@AllArgsConstructor // Constructor completo para tests y creación directa
public class ReservaRequestDTO {

    @NotNull(message = "El ID del cliente no puede ser nulo") // La reserva debe tener cliente
    @Min(value = 1, message = "El ID del cliente debe ser un número positivo") // ID válido
    private Long clienteId; // Referencia lógica al cliente en ms-clientes

    @NotNull(message = "El ID de la habitación no puede ser nulo") // La reserva necesita habitación
    @Min(value = 1, message = "El ID de la habitación debe ser un número positivo") // ID válido
    private Long habitacionId; // Referencia lógica a la habitación en ms-habitaciones

    @NotNull(message = "La fecha de ingreso no puede ser nula") // Fecha de inicio obligatoria
    @Future(message = "La fecha de ingreso debe ser una fecha futura") // No se puede reservar en el pasado
    private LocalDate fechaIngreso; // Fecha en que el huésped llegará al hotel

    @NotNull(message = "La fecha de salida no puede ser nula") // Fecha de salida obligatoria
    @Future(message = "La fecha de salida debe ser una fecha futura") // La salida debe ser en el futuro
    private LocalDate fechaSalida; // Fecha en que el huésped abandonará el hotel

    @NotBlank(message = "El estado de la reserva no puede estar vacío") // Estado obligatorio
    @Size(max = 20, message = "El estado no puede superar 20 caracteres") // PENDIENTE, CONFIRMADA, etc.
    private String estado; // Estado inicial de la reserva al ser creada

    @NotNull(message = "El número de pasajeros no puede ser nulo") // Pasajeros obligatorios
    @Min(value = 1, message = "Debe haber al menos 1 pasajero en la reserva") // Al menos 1 persona
    private Integer numeroPasajeros; // Número de huéspedes que se hospedarán

}

// Paquete de DTOs del microservicio ms-mantenimiento
package com.hotel.mantenimiento.dto;

import jakarta.validation.constraints.Min; // Valida que los IDs sean positivos
import jakarta.validation.constraints.NotBlank; // Valida que los Strings no estén vacíos
import jakarta.validation.constraints.NotNull; // Valida que los campos no sean null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha de solicitud no sea futura
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.time.LocalDate; // Tipo para las fechas de solicitud y completado

// DTO de ENTRADA para crear o actualizar una solicitud de mantenimiento de habitación
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserializar el JSON del body
@AllArgsConstructor // Constructor completo para tests
public class MantenimientoRequestDTO {

    @NotNull(message = "El ID de la habitación no puede ser nulo") // Habitación obligatoria
    @Min(value = 1, message = "El ID de la habitación debe ser positivo") // ID válido
    private Long habitacionId; // ID de la habitación que necesita mantenimiento

    @NotBlank(message = "La descripción del mantenimiento no puede estar vacía") // Descripción obligatoria
    @Size(max = 600, message = "La descripción no puede superar 600 caracteres") // Límite de la columna
    private String descripcion; // Descripción del problema o trabajo a realizar

    @NotBlank(message = "El estado no puede estar vacío") // Estado obligatorio
    @Size(max = 20, message = "El estado no puede superar 20 caracteres") // PENDIENTE, EN_PROCESO, etc.
    private String estado; // Estado actual de la solicitud de mantenimiento

    @NotBlank(message = "La prioridad no puede estar vacía") // Prioridad obligatoria
    @Size(max = 10, message = "La prioridad no puede superar 10 caracteres") // BAJA, MEDIA, ALTA, CRITICA
    private String prioridad; // Nivel de urgencia del mantenimiento

    @Min(value = 1, message = "El ID del empleado debe ser positivo") // ID válido si se asigna
    private Long empleadoId; // ID del empleado asignado (puede ser null al crear la solicitud)

    @NotNull(message = "La fecha de solicitud no puede ser nula") // Fecha obligatoria
    @PastOrPresent(message = "La fecha de solicitud no puede ser en el futuro") // Solo pasado o presente
    private LocalDate fechaSolicitud; // Fecha en que se registró la necesidad de mantenimiento

    private LocalDate fechaCompletado; // Fecha en que se completó el trabajo (null si no terminó)

}

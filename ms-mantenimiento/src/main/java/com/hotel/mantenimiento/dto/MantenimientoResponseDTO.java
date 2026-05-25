// Paquete de DTOs del microservicio ms-mantenimiento
package com.hotel.mantenimiento.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros para la respuesta
import lombok.Data; // Genera métodos de acceso y utilidad automáticamente
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.time.LocalDate; // Tipo para las fechas de solicitud y completado en la respuesta

// DTO de SALIDA: datos de la solicitud de mantenimiento devueltos en la respuesta HTTP
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en el Service
public class MantenimientoResponseDTO {

    private Long id; // ID único de la solicitud de mantenimiento generado por MySQL
    private Long habitacionId; // ID de la habitación que requiere mantenimiento
    private String descripcion; // Descripción del trabajo de mantenimiento
    private String estado; // Estado actual de la solicitud
    private String prioridad; // Nivel de urgencia del mantenimiento
    private Long empleadoId; // ID del empleado asignado (puede ser null)
    private LocalDate fechaSolicitud; // Fecha en que se creó la solicitud
    private LocalDate fechaCompletado; // Fecha en que se completó (null si aún en proceso)

}

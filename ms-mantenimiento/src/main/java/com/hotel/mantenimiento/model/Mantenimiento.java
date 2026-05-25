// Paquete de la capa de modelo del microservicio ms-mantenimiento
package com.hotel.mantenimiento.model;

// Importa las anotaciones JPA para mapear la entidad a MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Constructor vacío necesario para JPA
import lombok.AllArgsConstructor; // Constructor completo para uso en el Service
import java.time.LocalDate; // Tipo para fechas de solicitud y completado sin componente horaria

// ═══════════════════════════════════════════════════════════
// Mantenimiento.java — Entidad JPA del microservicio ms-mantenimiento
//
// Representa una solicitud de mantenimiento o reparación
// de una habitación del hotel.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos de acceso y utilidad automáticamente
@NoArgsConstructor // Constructor vacío obligatorio para que Hibernate cree instancias desde BD
@AllArgsConstructor // Constructor con todos los parámetros para el Service
@Entity // Declara esta clase como entidad JPA persistente
@Table(name = "mantenimientos") // Nombre de la tabla en db_hotel_mantenimiento
public class Mantenimiento {

    @Id // Columna PRIMARY KEY de la tabla mantenimientos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL genera el id con AUTO_INCREMENT
    private Long id; // Identificador único de la solicitud de mantenimiento

    // ID de la habitación que requiere mantenimiento (referencia lógica a ms-habitaciones)
    @Column(nullable = false) // La habitación es obligatoria para identificar dónde trabajar
    private Long habitacionId;

    // Descripción detallada del problema o trabajo a realizar
    @Column(nullable = false, length = 600) // Descripción obligatoria del trabajo de mantenimiento
    private String descripcion;

    // Estado actual de la solicitud (PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO)
    @Column(nullable = false, length = 20) // Estado obligatorio para el seguimiento de la tarea
    private String estado;

    // Nivel de urgencia del mantenimiento (BAJA, MEDIA, ALTA, CRITICA)
    @Column(nullable = false, length = 10) // Prioridad obligatoria para planificar el trabajo
    private String prioridad;

    // ID del empleado asignado para realizar el mantenimiento (referencia lógica a ms-empleados)
    @Column // El empleado puede asignarse posteriormente (opcional al crear la solicitud)
    private Long empleadoId;

    // Fecha en que se registró la solicitud de mantenimiento
    @Column(nullable = false) // La fecha de solicitud es obligatoria para el historial
    private LocalDate fechaSolicitud;

    // Fecha en que se completó el trabajo de mantenimiento (null si aún no ha terminado)
    @Column // La fecha de completado es opcional hasta que el trabajo finaliza
    private LocalDate fechaCompletado;

}

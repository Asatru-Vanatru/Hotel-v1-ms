// Paquete de acceso a datos del microservicio ms-mantenimiento
package com.hotel.mantenimiento.repository;

import com.hotel.mantenimiento.model.Mantenimiento; // Entidad de solicitudes de mantenimiento
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.time.LocalDate; // Tipo para filtrar por fechas de solicitud
import java.util.List; // Lista para múltiples solicitudes de mantenimiento

// Repositorio de acceso a la tabla "mantenimientos" en db_hotel_mantenimiento
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM mantenimientos WHERE estado = ?
    // Filtra solicitudes por estado: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
    List<Mantenimiento> findByEstado(String estado);

    // → SELECT * FROM mantenimientos WHERE habitacion_id = ?
    // Obtiene todas las solicitudes de mantenimiento de una habitación específica
    List<Mantenimiento> findByHabitacionId(Long habitacionId);

    // → SELECT * FROM mantenimientos WHERE empleado_id = ?
    // Obtiene todas las tareas asignadas a un empleado de mantenimiento
    List<Mantenimiento> findByEmpleadoId(Long empleadoId);

    // → SELECT * FROM mantenimientos WHERE prioridad = ? AND estado = 'PENDIENTE'
    // Busca solicitudes pendientes de alta prioridad para atención urgente
    List<Mantenimiento> findByPrioridadAndEstado(String prioridad, String estado);

    // → SELECT * FROM mantenimientos WHERE fecha_solicitud BETWEEN ? AND ?
    // Filtra solicitudes registradas en un período de tiempo
    List<Mantenimiento> findByFechaSolicitudBetween(LocalDate inicio, LocalDate fin);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Cuenta las solicitudes pendientes de alta prioridad para alertas en recepción
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.estado = 'PENDIENTE' AND m.prioridad IN ('ALTA', 'CRITICA')") // JPQL con IN para múltiples valores
    Long contarPendientesUrgentes(); // Número de solicitudes urgentes sin atender


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene las habitaciones con más solicitudes de mantenimiento (indica problemas recurrentes)
    @Query(
        value = "SELECT habitacion_id, COUNT(*) AS total_solicitudes FROM mantenimientos GROUP BY habitacion_id ORDER BY total_solicitudes DESC LIMIT 5", // SQL nativo con GROUP BY y LIMIT
        nativeQuery = true // SQL directo a MySQL
    )
    List<Object[]> findHabitacionesConMasMantenimientos(); // Top 5 habitaciones más problemáticas

}

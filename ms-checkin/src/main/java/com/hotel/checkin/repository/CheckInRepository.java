// Paquete de acceso a datos del microservicio ms-checkin
package com.hotel.checkin.repository;

import com.hotel.checkin.model.CheckIn; // Entidad que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD completo
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
//import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.time.LocalDateTime; // Tipo para buscar por rango de fecha/hora de check-in
import java.util.List; // Lista para resultados múltiples
import java.util.Optional; // Encapsula resultado único que puede no existir

// Repositorio de acceso a la tabla "checkins" en db_hotel_checkin
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM checkins WHERE reserva_id = ?
    // Busca el check-in asociado a una reserva específica
    Optional<CheckIn> findByReservaId(Long reservaId);

    // → SELECT * FROM checkins WHERE cliente_id = ?
    // Obtiene el historial de check-ins de un cliente
    List<CheckIn> findByClienteId(Long clienteId);

    // → SELECT * FROM checkins WHERE habitacion_id = ?
    // Obtiene todos los check-ins registrados en una habitación específica
    List<CheckIn> findByHabitacionId(Long habitacionId);

    // → SELECT * FROM checkins WHERE fecha_hora_check_in BETWEEN ? AND ?
    // Busca check-ins realizados en un período de tiempo específico
    List<CheckIn> findByFechaHoraCheckInBetween(LocalDateTime inicio, LocalDateTime fin);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Obtiene los check-ins de hoy ordenados por hora para el reporte de llegadas del día
    @Query("SELECT c FROM CheckIn c WHERE CAST(c.fechaHoraCheckIn AS date) = CURRENT_DATE ORDER BY c.fechaHoraCheckIn ASC") // JPQL filtro por fecha actual
    List<CheckIn> findCheckInsDeHoy(); // Check-ins registrados en el día actual


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Cuenta los check-ins realizados en el último mes para estadísticas de ocupación
    @Query(
        value = "SELECT COUNT(*) FROM checkins WHERE fecha_hora_check_in >= DATE_SUB(NOW(), INTERVAL 1 MONTH)", // SQL nativo con DATE_SUB de MySQL
        nativeQuery = true // SQL directo a MySQL, no JPQL
    )
    Long contarCheckInsUltimoMes(); // Cantidad de check-ins en los últimos 30 días

}

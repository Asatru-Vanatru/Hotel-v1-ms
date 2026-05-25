// Paquete de acceso a datos del microservicio ms-reservas
package com.hotel.reservas.repository;

import com.hotel.reservas.model.Reserva; // Entidad de reservas que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con operaciones CRUD
import org.springframework.data.jpa.repository.Query; // Para definir consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en consultas @Query
import java.time.LocalDate; // Tipo para las fechas de filtro en las búsquedas
import java.util.List; // Lista para múltiples resultados de reservas

// Repositorio de acceso a la tabla "reservas" en db_hotel_reservas
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM reservas WHERE cliente_id = ?
    // Obtiene todas las reservas de un cliente específico (historial de huésped)
    List<Reserva> findByClienteId(Long clienteId);

    // → SELECT * FROM reservas WHERE habitacion_id = ?
    // Obtiene todas las reservas de una habitación específica
    List<Reserva> findByHabitacionId(Long habitacionId);

    // → SELECT * FROM reservas WHERE estado = ?
    // Filtra reservas por estado: PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
    List<Reserva> findByEstado(String estado);

    // → SELECT * FROM reservas WHERE cliente_id = ? AND estado = ?
    // Busca reservas activas (CONFIRMADA) de un cliente específico
    List<Reserva> findByClienteIdAndEstado(Long clienteId, String estado);

    // → SELECT * FROM reservas WHERE fecha_ingreso >= ?
    // Busca reservas con fecha de ingreso a partir de una fecha dada
    List<Reserva> findByFechaIngresoGreaterThanEqual(LocalDate fecha);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Cuenta cuántas reservas activas (no canceladas) tiene una habitación en un período
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.habitacionId = :habitacionId AND r.estado != 'CANCELADA' AND r.fechaIngreso <= :fin AND r.fechaSalida >= :inicio") // JPQL para verificar disponibilidad
    Long contarReservasActivasEnPeriodo(
        @Param("habitacionId") Long habitacionId, // ID de la habitación a consultar
        @Param("inicio") LocalDate inicio,         // Inicio del período a verificar
        @Param("fin") LocalDate fin                // Fin del período a verificar
    );

    // Obtiene reservas ordenadas por fecha de ingreso más próxima (próximas llegadas)
    @Query("SELECT r FROM Reserva r WHERE r.estado = 'CONFIRMADA' ORDER BY r.fechaIngreso ASC") // JPQL con orden
    List<Reserva> findProximasLlegadas(); // Reservas confirmadas ordenadas por fecha de ingreso


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene el total de noches reservadas por un cliente (para calcular fidelidad)
    @Query(
        value = "SELECT COALESCE(SUM(DATEDIFF(fecha_salida, fecha_ingreso)), 0) FROM reservas WHERE cliente_id = :clienteId AND estado = 'COMPLETADA'", // SQL nativo MySQL con DATEDIFF
        nativeQuery = true // SQL nativo directo a MySQL
    )
    Long calcularTotalNochesCliente(@Param("clienteId") Long clienteId); // Total noches del historial

}

// Paquete de acceso a datos del microservicio ms-consumos
package com.hotel.consumos.repository;

import com.hotel.consumos.model.Consumo; // Entidad de consumos de servicios
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.math.BigDecimal; // Tipo para los totales calculados en consultas agregadas
import java.util.List; // Lista para múltiples registros de consumo

// Repositorio de acceso a la tabla "consumos" en db_hotel_consumos
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM consumos WHERE reserva_id = ?
    // Obtiene todos los consumos registrados para una reserva específica
    List<Consumo> findByReservaId(Long reservaId);

    // → SELECT * FROM consumos WHERE cliente_id = ?
    // Obtiene el historial de consumos de un cliente
    List<Consumo> findByClienteId(Long clienteId);

    // → SELECT * FROM consumos WHERE servicio_id = ?
    // Obtiene todos los registros de un servicio específico consumido
    List<Consumo> findByServicioId(Long servicioId);

    // → SELECT * FROM consumos WHERE reserva_id = ? AND servicio_id = ?
    // Busca consumos específicos de un servicio dentro de una reserva
    List<Consumo> findByReservaIdAndServicioId(Long reservaId, Long servicioId);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Calcula el total de consumos de una reserva (suma de todos los subtotales)
    @Query("SELECT COALESCE(SUM(c.subtotal), 0) FROM Consumo c WHERE c.reservaId = :reservaId") // JPQL con SUM y COALESCE
    BigDecimal calcularTotalConsumosPorReserva(@Param("reservaId") Long reservaId); // Total de cargos de la reserva


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene los servicios más consumidos en el hotel (ranking para análisis de demanda)
    @Query(
        value = "SELECT servicio_id, SUM(cantidad) AS total_consumido FROM consumos GROUP BY servicio_id ORDER BY total_consumido DESC LIMIT 5", // SQL nativo con GROUP BY y ORDER
        nativeQuery = true // SQL directo a MySQL
    )
    List<Object[]> findServiciosMasConsumidos(); // Top 5 servicios más solicitados por huéspedes

}

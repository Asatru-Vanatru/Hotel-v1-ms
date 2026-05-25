// Paquete de acceso a datos del microservicio ms-pagos
package com.hotel.pagos.repository;

import com.hotel.pagos.model.Pago; // Entidad de pagos que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD completo
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.math.BigDecimal; // Tipo para totales de ingresos en consultas agregadas
import java.util.List; // Lista para múltiples registros de pago

// Repositorio de acceso a la tabla "pagos" en db_hotel_pagos
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM pagos WHERE reserva_id = ?
    // Obtiene todos los pagos realizados para una reserva específica
    List<Pago> findByReservaId(Long reservaId);

    // → SELECT * FROM pagos WHERE cliente_id = ?
    // Obtiene el historial de pagos de un cliente
    List<Pago> findByClienteId(Long clienteId);

    // → SELECT * FROM pagos WHERE estado = ?
    // Filtra pagos por estado: PENDIENTE, COMPLETADO, CANCELADO, REEMBOLSADO
    List<Pago> findByEstado(String estado);

    // → SELECT * FROM pagos WHERE metodo_pago = ?
    // Filtra pagos por método: EFECTIVO, TARJETA_CREDITO, TRANSFERENCIA, etc.
    List<Pago> findByMetodoPago(String metodoPago);

    // → SELECT * FROM pagos WHERE cliente_id = ? AND estado = ?
    // Obtiene los pagos completados de un cliente específico
    List<Pago> findByClienteIdAndEstado(Long clienteId, String estado);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Calcula el total de pagos completados de una reserva
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.reservaId = :reservaId AND p.estado = 'COMPLETADO'") // JPQL con SUM y filtro de estado
    BigDecimal calcularTotalPagadoPorReserva(@Param("reservaId") Long reservaId); // Total abonado por la reserva


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Suma total de ingresos por método de pago para reporte de tesorería
    @Query(
        value = "SELECT metodo_pago, SUM(monto) AS total FROM pagos WHERE estado = 'COMPLETADO' GROUP BY metodo_pago ORDER BY total DESC", // SQL nativo con GROUP BY para reporte
        nativeQuery = true // SQL directo a MySQL para el reporte contable
    )
    List<Object[]> calcularIngresosPorMetodoPago(); // Distribución de ingresos por método de pago

}

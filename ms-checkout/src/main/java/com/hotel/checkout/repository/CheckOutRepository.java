// Paquete de acceso a datos del microservicio ms-checkout
package com.hotel.checkout.repository;

import com.hotel.checkout.model.CheckOut; // Entidad que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con operaciones CRUD
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.math.BigDecimal; // Tipo para el total de ingresos en consultas agregadas
import java.util.List; // Lista para múltiples resultados
import java.util.Optional; // Encapsula resultado único que puede no existir

// Repositorio de acceso a la tabla "checkouts" en db_hotel_checkout
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM checkouts WHERE reserva_id = ?
    // Busca el check-out asociado a una reserva específica
    Optional<CheckOut> findByReservaId(Long reservaId);

    // → SELECT * FROM checkouts WHERE cliente_id = ?
    // Obtiene el historial de check-outs de un cliente
    List<CheckOut> findByClienteId(Long clienteId);

    // → SELECT * FROM checkouts WHERE habitacion_id = ?
    // Obtiene el historial de check-outs de una habitación específica
    List<CheckOut> findByHabitacionId(Long habitacionId);

    // → SELECT * FROM checkouts WHERE dias_estancia >= ?
    // Busca estadías largas (más de X noches) para análisis de comportamiento
    List<CheckOut> findByDiasEstanciaGreaterThanEqual(Integer dias);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Calcula la suma total de ingresos de todos los check-outs completados
    @Query("SELECT SUM(c.totalFinal) FROM CheckOut c") // JPQL con función de agregación SUM
    BigDecimal calcularIngresosTotales(); // Total acumulado de todos los cobros registrados


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene el promedio de días de estadía para análisis de ocupación del hotel
    @Query(
        value = "SELECT ROUND(AVG(dias_estancia), 2) FROM checkouts", // SQL nativo con AVG de MySQL
        nativeQuery = true // Consulta SQL directo a MySQL
    )
    Double calcularPromedioEstancia(); // Promedio de noches por estadía para estadísticas

}

// Paquete de acceso a datos del microservicio ms-habitaciones
package com.hotel.habitaciones.repository;

import com.hotel.habitaciones.model.Habitacion; // Entidad que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD completo
import org.springframework.data.jpa.repository.Query; // Anotación para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.math.BigDecimal; // Tipo del precio por noche para las búsquedas por rango
import java.util.List; // Lista para resultados múltiples
import java.util.Optional; // Encapsula resultado único que puede no existir

// Repositorio de acceso a la tabla "habitaciones" en db_hotel_habitaciones
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM habitaciones WHERE estado = ?
    // Busca habitaciones por estado: DISPONIBLE, OCUPADA, MANTENIMIENTO, etc.
    List<Habitacion> findByEstado(String estado);

    // → SELECT * FROM habitaciones WHERE tipo = ?
    // Busca habitaciones por tipo: SIMPLE, DOBLE, SUITE, FAMILIAR, etc.
    List<Habitacion> findByTipo(String tipo);

    // → SELECT * FROM habitaciones WHERE numero = ?
    // Busca una habitación por su número visible (ej: "101", "305B")
    Optional<Habitacion> findByNumero(String numero);

    // → SELECT * FROM habitaciones WHERE capacidad >= ?
    // Busca habitaciones que puedan alojar al menos X personas
    List<Habitacion> findByCapacidadGreaterThanEqual(Integer capacidad);

    // → SELECT * FROM habitaciones WHERE piso = ?
    // Busca todas las habitaciones en un piso específico del hotel
    List<Habitacion> findByPiso(Integer piso);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Busca habitaciones disponibles de un tipo específico (lógica combinada)
    @Query("SELECT h FROM Habitacion h WHERE h.tipo = :tipo AND h.estado = 'DISPONIBLE'") // JPQL combinado
    List<Habitacion> findDisponiblesPorTipo(@Param("tipo") String tipo); // Habitaciones disponibles del tipo

    // Busca habitaciones cuyo precio por noche esté dentro de un rango de precios
    @Query("SELECT h FROM Habitacion h WHERE h.precioNoche BETWEEN :min AND :max ORDER BY h.precioNoche ASC") // JPQL con rango y orden
    List<Habitacion> findByRangoPrecio(@Param("min") BigDecimal min, @Param("max") BigDecimal max); // Parámetros del rango


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene todas las habitaciones disponibles ordenadas por precio ascendente (SQL nativo)
    @Query(
        value = "SELECT * FROM habitaciones WHERE estado = 'DISPONIBLE' ORDER BY precio_noche ASC", // SQL nativo MySQL
        nativeQuery = true // Indica SQL nativo directo a MySQL
    )
    List<Habitacion> findTodasDisponiblesOrdenadas(); // Listado ordenado para búsqueda de precios

}

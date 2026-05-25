// Paquete de acceso a datos del microservicio ms-servicios
package com.hotel.servicios.repository;

import com.hotel.servicios.model.Servicio; // Entidad del catálogo de servicios
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.math.BigDecimal; // Tipo para filtrar servicios por rango de precio
import java.util.List; // Lista para múltiples resultados del catálogo

// Repositorio de acceso a la tabla "servicios" en db_hotel_servicios
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM servicios WHERE disponible = ?
    // Obtiene solo los servicios disponibles para solicitar (catálogo activo)
    List<Servicio> findByDisponible(Boolean disponible);

    // → SELECT * FROM servicios WHERE LOWER(categoria) = LOWER(?)
    // Filtra el catálogo por categoría: RESTAURANTE, SPA, LAVANDERIA, etc.
    List<Servicio> findByCategoriaIgnoreCase(String categoria);

    // → SELECT * FROM servicios WHERE disponible = true AND categoria = ?
    // Obtiene servicios disponibles de una categoría específica
    List<Servicio> findByCategoriaIgnoreCaseAndDisponible(String categoria, Boolean disponible);

    // → SELECT * FROM servicios WHERE UPPER(nombre) LIKE UPPER('%?%')
    // Busca servicios cuyo nombre contenga el texto dado
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Busca servicios cuyo precio esté dentro de un rango accesible para el huésped
    @Query("SELECT s FROM Servicio s WHERE s.precio <= :precioMax AND s.disponible = true ORDER BY s.precio ASC") // JPQL con filtro de precio y disponibilidad
    List<Servicio> findDisponiblesHastaPreio(@Param("precioMax") BigDecimal precioMax); // Servicios asequibles


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Obtiene el catálogo completo disponible ordenado por categoría y luego por precio
    @Query(
        value = "SELECT * FROM servicios WHERE disponible = 1 ORDER BY categoria ASC, precio ASC", // SQL nativo con ordenamiento múltiple
        nativeQuery = true // Consulta SQL directo a la base de datos MySQL
    )
    List<Servicio> findCatalogoDisponibleOrdenado(); // Catálogo completo ordenado para la recepción

}

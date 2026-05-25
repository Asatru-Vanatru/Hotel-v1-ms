// Paquete de acceso a datos del microservicio ms-empleados
package com.hotel.empleados.repository;

import com.hotel.empleados.model.Empleado; // Entidad de empleados que este repositorio gestiona
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base con CRUD
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Enlaza parámetros en @Query
import java.util.List; // Lista para múltiples resultados
import java.util.Optional; // Encapsula resultado único que puede no existir

// Repositorio de acceso a la tabla "empleados" en db_hotel_empleados
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    // → SELECT * FROM empleados WHERE LOWER(departamento) = LOWER(?)
    // Obtiene todos los empleados de un departamento del hotel
    List<Empleado> findByDepartamentoIgnoreCase(String departamento);

    // → SELECT * FROM empleados WHERE LOWER(cargo) = LOWER(?)
    // Obtiene empleados por su cargo o puesto de trabajo
    List<Empleado> findByCargoIgnoreCase(String cargo);

    // → SELECT * FROM empleados WHERE email = ?
    // Busca un empleado por su email corporativo único
    Optional<Empleado> findByEmail(String email);

    // → SELECT * FROM empleados WHERE UPPER(nombre) LIKE UPPER('%?%')
    // Busca empleados cuyo nombre contenga el texto dado
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    // → SELECT * FROM empleados WHERE departamento = ? AND cargo = ?
    // Busca empleados por departamento y cargo específico (ej: LIMPIEZA + Ama de Llaves)
    List<Empleado> findByDepartamentoIgnoreCaseAndCargoIgnoreCase(String departamento, String cargo);


    // ── @QUERY JPQL ──────────────────────────────────────────────────────────

    // Busca empleados contratados en un año específico
    @Query("SELECT e FROM Empleado e WHERE YEAR(e.fechaContratacion) = :anio ORDER BY e.fechaContratacion ASC") // JPQL con función YEAR
    List<Empleado> findByAnioContratacion(@Param("anio") Integer anio); // Empleados de un año de contratación


    // ── SQL NATIVO ────────────────────────────────────────────────────────────

    // Cuenta cuántos empleados hay por departamento para el organigrama del hotel
    @Query(
        value = "SELECT departamento, COUNT(*) AS total FROM empleados GROUP BY departamento ORDER BY total DESC", // SQL nativo con GROUP BY para estadísticas
        nativeQuery = true // SQL directo a MySQL
    )
    List<Object[]> contarEmpleadosPorDepartamento(); // Distribución del personal por área

}

// Paquete de acceso a datos del microservicio ms-clientes
package com.hotel.clientes.repository;

// Importa la entidad que este repositorio gestiona en la base de datos
import com.hotel.clientes.model.Cliente;
// Importa la interfaz base de Spring Data JPA que provee CRUD completo sin código adicional
import org.springframework.data.jpa.repository.JpaRepository;
// Importa la anotación para definir consultas JPQL o SQL nativo personalizadas
import org.springframework.data.jpa.repository.Query;
// Importa la anotación para enlazar parámetros nombrados en consultas @Query
import org.springframework.data.repository.query.Param;

// Importa la lista de Java para retornar múltiples resultados
import java.util.List;
// Importa Optional para encapsular un resultado que puede o no existir
import java.util.Optional;

// ═══════════════════════════════════════════════════════════
// ClienteRepository.java
// Acceso a datos de la tabla "clientes" en MySQL.
//
// JpaRepository<Cliente, Long> hereda automáticamente:
//   save()        → INSERT o UPDATE
//   findById()    → SELECT WHERE id = ?
//   findAll()     → SELECT * FROM clientes
//   deleteById()  → DELETE WHERE id = ?
//   count()       → SELECT COUNT(*)
//   existsById()  → verificar si existe un id
//
// Tres tipos de consultas personalizadas demostradas:
//   1. Query Methods (derivadas del nombre del método)
//   2. @Query con JPQL (lenguaje orientado a objetos)
//   3. @Query con SQL nativo (SQL directo a MySQL)
// ═══════════════════════════════════════════════════════════

// JpaRepository<Entidad, TipoDeLaClavePrimaria>
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // ── TIPO 1: QUERY METHODS ────────────────────────────────────────────────
    // Spring Data analiza el nombre del método y genera el SQL automáticamente.
    // Los atributos deben coincidir con los campos de la entidad Cliente.java.

    // → SELECT * FROM clientes WHERE UPPER(nombre) LIKE UPPER('%?%')
    // Busca clientes cuyo nombre contenga el texto (sin importar mayúsculas)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // → SELECT * FROM clientes WHERE UPPER(apellido) LIKE UPPER('%?%')
    // Busca clientes cuyo apellido contenga el texto dado
    List<Cliente> findByApellidoContainingIgnoreCase(String apellido);

    // → SELECT * FROM clientes WHERE email = ?
    // Busca un cliente por su email (único), retorna Optional para manejar el caso 404
    Optional<Cliente> findByEmail(String email);

    // → SELECT * FROM clientes WHERE LOWER(nacionalidad) = LOWER(?)
    // Busca todos los clientes de una misma nacionalidad (sin distinción de mayúsculas)
    List<Cliente> findByNacionalidadIgnoreCase(String nacionalidad);


    // ── TIPO 2: @QUERY CON JPQL ─────────────────────────────────────────────
    // Consulta en lenguaje JPQL: opera sobre entidades Java, no sobre tablas.
    // "Cliente" = clase Java, "c.tipoDocumento" = atributo de la clase Cliente.
    // Hibernate traduce este JPQL al SQL correcto según el dialecto de MySQL.

    // Busca clientes por tipo de documento (DNI, PASAPORTE, CEDULA, etc.)
    @Query("SELECT c FROM Cliente c WHERE c.tipoDocumento = :tipo") // JPQL sobre la entidad Cliente
    List<Cliente> findByTipoDocumento(@Param("tipo") String tipo); // @Param enlaza :tipo con el parámetro

    // Busca un cliente por número de documento (referencia cruzada con su tipo)
    @Query("SELECT c FROM Cliente c WHERE c.numeroDocumento = :numero") // JPQL por número de documento
    Optional<Cliente> findByNumeroDocumento(@Param("numero") String numero); // Retorna Optional


    // ── TIPO 3: SQL NATIVO ───────────────────────────────────────────────────
    // nativeQuery=true: Hibernate envía el SQL tal cual al motor MySQL.
    // Útil para funciones específicas de MySQL como CONCAT, LOWER, MATCH AGAINST, etc.

    // Búsqueda por nombre completo (nombre + apellido) sin distinción de mayúsculas
    @Query(
        value = "SELECT * FROM clientes WHERE LOWER(CONCAT(nombre, ' ', apellido)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))", // SQL nativo MySQL
        nativeQuery = true // Indica que es SQL nativo y no JPQL
    )
    List<Cliente> buscarPorNombreCompleto(@Param("nombreCompleto") String nombreCompleto); // Parámetro del SQL

}

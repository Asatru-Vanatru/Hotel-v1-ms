// Paquete de la capa de modelo del microservicio ms-habitaciones
package com.hotel.habitaciones.model;

// Importa todas las anotaciones JPA para mapeo objeto-relacional
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Genera constructor vacío requerido por JPA
import lombok.AllArgsConstructor; // Genera constructor con todos los campos
import java.math.BigDecimal; // Tipo adecuado para valores monetarios con precisión exacta

// ═══════════════════════════════════════════════════════════
// Habitacion.java — Entidad JPA del microservicio ms-habitaciones
//
// Representa el catálogo de habitaciones físicas del hotel.
// Hibernate la mapea a la tabla "habitaciones" en MySQL.
// ═══════════════════════════════════════════════════════════

// @Data: genera todos los métodos de acceso y utilidad automáticamente
@Data
// @NoArgsConstructor: constructor vacío que JPA necesita para crear instancias desde ResultSet
@NoArgsConstructor
// @AllArgsConstructor: constructor con todos los campos para uso en el Service
@AllArgsConstructor
// @Entity: declara esta clase como entidad persistente de JPA
@Entity
// @Table: especifica el nombre de la tabla en la base de datos MySQL
@Table(name = "habitaciones")
public class Habitacion {

    // @Id: campo que representa la PRIMARY KEY de la tabla habitaciones
    @Id
    // @GeneratedValue IDENTITY: MySQL genera el valor con AUTO_INCREMENT en cada INSERT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la habitación

    // Número de habitación visible para los huéspedes (ej: "101", "202A")
    @Column(nullable = false, unique = true, length = 10) // Número único obligatorio
    private String numero;

    // Categoría de la habitación (SIMPLE, DOBLE, SUITE, FAMILIAR, etc.)
    @Column(nullable = false, length = 30) // Tipo de habitación obligatorio
    private String tipo;

    // Máximo número de personas que pueden hospedarse en la habitación
    @Column(nullable = false) // Capacidad obligatoria para validar reservas
    private Integer capacidad;

    // Precio por noche de la habitación en moneda local (BigDecimal para precisión monetaria)
    @Column(nullable = false, precision = 10, scale = 2) // Precio con 2 decimales obligatorio
    private BigDecimal precioNoche;

    // Descripción detallada de las amenidades e instalaciones de la habitación
    @Column(length = 500) // Descripción opcional, máximo 500 caracteres
    private String descripcion;

    // Estado actual de la habitación (DISPONIBLE, OCUPADA, MANTENIMIENTO, FUERA_SERVICIO)
    @Column(nullable = false, length = 20) // Estado obligatorio para controlar la disponibilidad
    private String estado;

    // Piso del hotel donde se encuentra la habitación
    @Column // Número de piso opcional
    private Integer piso;

}

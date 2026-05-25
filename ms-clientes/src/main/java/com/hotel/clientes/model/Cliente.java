// Paquete de la capa de modelo del microservicio ms-clientes
package com.hotel.clientes.model;

// Importa las anotaciones JPA necesarias para mapear la clase a una tabla MySQL
import jakarta.persistence.*;
// @Data: genera getters, setters, equals, hashCode y toString automáticamente
import lombok.Data;
// @NoArgsConstructor: genera constructor sin argumentos (requerido por JPA/Hibernate)
import lombok.NoArgsConstructor;
// @AllArgsConstructor: genera constructor con todos los campos (útil para crear objetos)
import lombok.AllArgsConstructor;

// ═══════════════════════════════════════════════════════════
// Cliente.java — Entidad JPA del microservicio ms-clientes
//
// Hibernate convierte esta clase en la tabla "clientes"
// de la base de datos db_hotel_clientes en MySQL.
//
// REGLA ARQUITECTURAL:
//   Las validaciones de negocio (@NotBlank, @Email, etc.)
//   NO van aquí. Van en ClienteRequestDTO.java.
//   La entidad solo define restricciones de base de datos
//   (@Column nullable=false, unique=true).
// ═══════════════════════════════════════════════════════════

// @Data: genera automáticamente todos los métodos estándar de un POJO
@Data
// @NoArgsConstructor: constructor vacío obligatorio para que Hibernate reconstruya objetos desde BD
@NoArgsConstructor
// @AllArgsConstructor: constructor completo útil para instanciar objetos en el Service
@AllArgsConstructor
// @Entity: le indica a Hibernate que esta clase representa una tabla en la base de datos
@Entity
// @Table: define el nombre exacto de la tabla en MySQL (si se omite, usa el nombre de la clase)
@Table(name = "clientes")
public class Cliente {

    // @Id: define este campo como PRIMARY KEY de la tabla
    @Id
    // @GeneratedValue IDENTITY: MySQL asigna el id con AUTO_INCREMENT al hacer INSERT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del cliente en la base de datos

    // @Column nullable=false: genera restricción NOT NULL en la tabla MySQL
    @Column(nullable = false, length = 80) // Nombre del cliente, obligatorio, máximo 80 caracteres
    private String nombre;

    // Apellido del cliente, obligatorio en la base de datos
    @Column(nullable = false, length = 80) // Apellido obligatorio, máximo 80 caracteres
    private String apellido;

    // @Column unique=true: crea un índice UNIQUE en MySQL, no puede repetirse el email
    @Column(nullable = false, unique = true, length = 120) // Email único del cliente
    private String email;

    // Teléfono de contacto del cliente para notificaciones de reserva
    @Column(length = 20) // Teléfono opcional, máximo 20 caracteres
    private String telefono;

    // Tipo de documento de identidad (DNI, PASAPORTE, CEDULA, etc.)
    @Column(length = 30) // Tipo de documento, máximo 30 caracteres
    private String tipoDocumento;

    // Número del documento de identidad del huésped
    @Column(length = 30) // Número de documento, máximo 30 caracteres
    private String numeroDocumento;

    // País de origen o residencia del cliente
    @Column(length = 60) // Nacionalidad del cliente, máximo 60 caracteres
    private String nacionalidad;

}

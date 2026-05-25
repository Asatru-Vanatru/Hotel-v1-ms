// Paquete de la capa de modelo del microservicio ms-reservas
package com.hotel.reservas.model;

// Importa todas las anotaciones de Jakarta Persistence (JPA)
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Genera constructor sin parámetros (obligatorio para JPA)
import lombok.AllArgsConstructor; // Genera constructor con todos los parámetros
import java.math.BigDecimal; // Tipo exacto para cálculos monetarios, evita errores de redondeo
import java.time.LocalDate; // Tipo para fechas sin hora (fecha de ingreso y salida)

// ═══════════════════════════════════════════════════════════
// Reserva.java — Entidad JPA del microservicio ms-reservas
//
// Registra cada reserva de habitación vinculando un cliente
// con una habitación y un período de estadía.
// Los IDs de cliente y habitación son REFERENCIAS LÓGICAS:
// no hay @ForeignKey real (arquitectura de microservicios).
// ═══════════════════════════════════════════════════════════

// @Data: genera automáticamente todos los métodos de la clase de datos
@Data
// @NoArgsConstructor: constructor vacío requerido por Hibernate para reconstruir entidades
@NoArgsConstructor
// @AllArgsConstructor: constructor con todos los campos para instanciar en el Service
@AllArgsConstructor
// @Entity: indica a JPA que esta clase es una tabla en la base de datos
@Entity
// @Table: define el nombre de la tabla en db_hotel_reservas
@Table(name = "reservas")
public class Reserva {

    // @Id: columna PRIMARY KEY de la tabla reservas
    @Id
    // @GeneratedValue IDENTITY: MySQL genera el id automáticamente con AUTO_INCREMENT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la reserva

    // ID del cliente que realiza la reserva (referencia lógica a ms-clientes)
    // No es @ManyToOne porque los microservicios no comparten base de datos
    @Column(nullable = false) // El id del cliente es obligatorio
    private Long clienteId;

    // ID de la habitación reservada (referencia lógica a ms-habitaciones)
    // No hay JOIN directo entre bases de datos distintas en arquitectura de microservicios
    @Column(nullable = false) // El id de la habitación es obligatorio
    private Long habitacionId;

    // Fecha en que el huésped ingresará al hotel
    @Column(nullable = false) // La fecha de ingreso es obligatoria para la reserva
    private LocalDate fechaIngreso;

    // Fecha en que el huésped dejará el hotel
    @Column(nullable = false) // La fecha de salida es obligatoria para calcular costo
    private LocalDate fechaSalida;

    // Estado actual de la reserva (PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA)
    @Column(nullable = false, length = 20) // Estado obligatorio para el ciclo de vida de la reserva
    private String estado;

    // Monto estimado total de la reserva calculado al momento de crearla
    @Column(nullable = false, precision = 10, scale = 2) // Total estimado con 2 decimales
    private BigDecimal totalEstimado;

    // Número de pasajeros que se hospedarán (no puede exceder la capacidad de la habitación)
    @Column(nullable = false) // Número de pasajeros obligatorio para validar capacidad
    private Integer numeroPasajeros;

}

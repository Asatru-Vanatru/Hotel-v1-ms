// Paquete de la capa de modelo del microservicio ms-pagos
package com.hotel.pagos.model;

// Importa las anotaciones JPA para el mapeo objeto-relacional con MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Constructor vacío requerido por JPA
import lombok.AllArgsConstructor; // Constructor completo para instanciar en el Service
import java.math.BigDecimal; // Tipo preciso para el monto del pago sin errores de redondeo
import java.time.LocalDateTime; // Tipo para la fecha y hora exacta del pago registrado

// ═══════════════════════════════════════════════════════════
// Pago.java — Entidad JPA del microservicio ms-pagos
//
// Registra cada transacción de pago realizada por un
// huésped para liquidar su reserva y servicios.
// ═══════════════════════════════════════════════════════════

@Data // Genera automáticamente todos los métodos de acceso y utilidad
@NoArgsConstructor // Constructor vacío obligatorio para la reconstrucción de objetos por JPA
@AllArgsConstructor // Constructor con todos los parámetros para uso en la capa de servicio
@Entity // Indica a JPA que esta clase es una entidad persistente
@Table(name = "pagos") // Nombre de la tabla en la base de datos db_hotel_pagos
public class Pago {

    @Id // Columna PRIMARY KEY de la tabla pagos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT de MySQL
    private Long id; // Identificador único de la transacción de pago

    // ID de la reserva que se está pagando (referencia lógica a ms-reservas)
    @Column(nullable = false) // La reserva es obligatoria para identificar qué se está pagando
    private Long reservaId;

    // ID del cliente que realiza el pago (referencia lógica a ms-clientes)
    @Column(nullable = false) // El cliente es obligatorio para registrar a quién se cobra
    private Long clienteId;

    // Monto total pagado en esta transacción
    @Column(nullable = false, precision = 10, scale = 2) // Monto monetario preciso obligatorio
    private BigDecimal monto;

    // Forma de pago utilizada (EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA)
    @Column(nullable = false, length = 30) // Método de pago obligatorio para el registro contable
    private String metodoPago;

    // Estado actual del pago (PENDIENTE, COMPLETADO, CANCELADO, REEMBOLSADO)
    @Column(nullable = false, length = 20) // Estado obligatorio para el ciclo de vida del pago
    private String estado;

    // Fecha y hora exacta en que se realizó o registró el pago
    @Column(nullable = false) // La fecha del pago es obligatoria para registros contables
    private LocalDateTime fechaPago;

    // Referencia o número de comprobante externo (boleta, voucher, número de transacción)
    @Column(length = 100) // Referencia opcional del comprobante de pago
    private String referencia;

}

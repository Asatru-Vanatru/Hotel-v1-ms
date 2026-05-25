// Paquete de la capa de modelo del microservicio ms-checkin
package com.hotel.checkin.model;

// Importa las anotaciones JPA para mapeo objeto-relacional con MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters y métodos de utilidad
import lombok.NoArgsConstructor; // Constructor vacío requerido por JPA/Hibernate
import lombok.AllArgsConstructor; // Constructor completo para instanciar en el Service
import java.time.LocalDateTime; // Tipo para fecha y hora exactas del momento del check-in

// ═══════════════════════════════════════════════════════════
// CheckIn.java — Entidad JPA del microservicio ms-checkin
//
// Registra el evento de entrada de un huésped al hotel,
// vinculando la reserva con la habitación y el cliente.
// Guarda el momento exacto del check-in.
// ═══════════════════════════════════════════════════════════

// @Data: genera todos los métodos de acceso y comparación
@Data
// @NoArgsConstructor: Hibernate lo necesita para crear objetos al leer filas de MySQL
@NoArgsConstructor
// @AllArgsConstructor: facilita crear instancias con todos los valores en el Service
@AllArgsConstructor
// @Entity: declara esta clase como entidad persistente en la base de datos
@Entity
// @Table: nombre de la tabla en db_hotel_checkin
@Table(name = "checkins")
public class CheckIn {

    // @Id: PRIMARY KEY de la tabla checkins
    @Id
    // @GeneratedValue IDENTITY: AUTO_INCREMENT de MySQL asigna el id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del registro de check-in

    // ID de la reserva asociada (referencia lógica a ms-reservas, sin JOIN entre BDs)
    @Column(nullable = false) // La reserva es obligatoria para realizar el check-in
    private Long reservaId;

    // ID del cliente que hace el check-in (referencia lógica a ms-clientes)
    @Column(nullable = false) // El cliente es obligatorio para identificar al huésped
    private Long clienteId;

    // ID de la habitación asignada (referencia lógica a ms-habitaciones)
    @Column(nullable = false) // La habitación es obligatoria para saber dónde se aloja el huésped
    private Long habitacionId;

    // Fecha y hora exacta en que el huésped realizó el check-in en recepción
    @Column(nullable = false) // El momento del check-in es obligatorio para el registro
    private LocalDateTime fechaHoraCheckIn;

    // Observaciones adicionales del recepcionista durante el proceso de check-in
    @Column(length = 400) // Observaciones opcionales, máximo 400 caracteres
    private String observaciones;

}

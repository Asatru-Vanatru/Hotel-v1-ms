// Paquete de la capa de modelo del microservicio ms-consumos
package com.hotel.consumos.model;

// Importa las anotaciones JPA para persistencia con MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera todos los métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío requerido por JPA
import lombok.AllArgsConstructor; // Constructor completo para el Service
import java.math.BigDecimal; // Tipo monetario exacto para el subtotal del consumo
import java.time.LocalDateTime; // Tipo para la fecha y hora exacta del consumo

// ═══════════════════════════════════════════════════════════
// Consumo.java — Entidad JPA del microservicio ms-consumos
//
// Registra cada vez que un huésped consume un servicio
// adicional durante su estadía (relaciona reserva + servicio).
// ═══════════════════════════════════════════════════════════

@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío que Hibernate necesita para reconstruir objetos
@AllArgsConstructor // Constructor completo para crear instancias en el Service
@Entity // Declara esta clase como entidad persistente en la base de datos
@Table(name = "consumos") // Nombre de la tabla en db_hotel_consumos
public class Consumo {

    @Id // Clave primaria de la tabla consumos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL genera el id con AUTO_INCREMENT
    private Long id; // Identificador único del consumo registrado

    // ID de la reserva activa durante la cual se realizó el consumo (referencia lógica a ms-reservas)
    @Column(nullable = false) // La reserva es obligatoria para asociar el consumo a una estadía
    private Long reservaId;

    // ID del cliente que consumió el servicio (referencia lógica a ms-clientes)
    @Column(nullable = false) // El cliente es obligatorio para identificar al consumidor
    private Long clienteId;

    // ID del servicio adicional consumido (referencia lógica a ms-servicios)
    @Column(nullable = false) // El servicio consumido es obligatorio para calcular el cobro
    private Long servicioId;

    // Cantidad de unidades del servicio consumidas (ej: 2 masajes, 3 desayunos)
    @Column(nullable = false) // La cantidad es obligatoria para calcular el subtotal
    private Integer cantidad;

    // Momento exacto en que se registró el consumo del servicio
    @Column(nullable = false) // La fecha y hora del consumo son obligatorias para el historial
    private LocalDateTime fechaConsumo;

    // Subtotal calculado: precio_unitario_servicio × cantidad
    @Column(nullable = false, precision = 10, scale = 2) // Subtotal monetario con 2 decimales
    private BigDecimal subtotal;

    // Notas adicionales sobre el consumo (alergias, preferencias especiales, etc.)
    @Column(length = 300) // Observaciones opcionales del consumo
    private String observaciones;

}

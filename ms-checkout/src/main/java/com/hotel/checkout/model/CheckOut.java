// Paquete de la capa de modelo del microservicio ms-checkout
package com.hotel.checkout.model;

// Importa las anotaciones JPA para mapear la clase a la tabla MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters, equals, hashCode y toString automáticamente
import lombok.NoArgsConstructor; // Constructor vacío necesario para JPA
import lombok.AllArgsConstructor; // Constructor con todos los campos
import java.math.BigDecimal; // Tipo preciso para representar el total final del cobro
import java.time.LocalDateTime; // Tipo para registrar la fecha y hora exacta del check-out

// ═══════════════════════════════════════════════════════════
// CheckOut.java — Entidad JPA del microservicio ms-checkout
//
// Registra el proceso de salida del huésped, calculando
// el total final incluyendo habitación y servicios.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos de acceso y utilidad
@NoArgsConstructor // Constructor vacío requerido por JPA para reconstruir objetos desde BD
@AllArgsConstructor // Constructor completo para crear instancias en la capa de servicio
@Entity // Declara esta clase como entidad persistente de JPA
@Table(name = "checkouts") // Nombre de la tabla en la base de datos db_hotel_checkout
public class CheckOut {

    @Id // Clave primaria de la tabla checkouts
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT de MySQL
    private Long id; // Identificador único del registro de check-out

    // ID de la reserva que se está cerrando (referencia lógica a ms-reservas)
    @Column(nullable = false) // La reserva es obligatoria para identificar la estadía
    private Long reservaId;

    // ID del cliente que realizó el check-out (referencia lógica a ms-clientes)
    @Column(nullable = false) // El cliente es obligatorio para el registro
    private Long clienteId;

    // ID de la habitación que se está liberando (referencia lógica a ms-habitaciones)
    @Column(nullable = false) // La habitación es obligatoria para marcarla como disponible
    private Long habitacionId;

    // Fecha y hora exacta en que el huésped abandonó el hotel
    @Column(nullable = false) // El momento del check-out es obligatorio para el cierre
    private LocalDateTime fechaHoraCheckOut;

    // Total final cobrado al huésped (habitación + servicios adicionales consumidos)
    @Column(nullable = false, precision = 10, scale = 2) // Total con 2 decimales, obligatorio
    private BigDecimal totalFinal;

    // Número de noches de estadía calculado desde check-in hasta check-out
    @Column(nullable = false) // Los días de estadía son obligatorios para verificar el cobro
    private Integer diasEstancia;

}

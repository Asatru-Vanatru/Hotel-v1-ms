// Paquete de DTOs del microservicio ms-checkin
package com.hotel.checkin.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros para la respuesta
import lombok.Data; // Genera métodos de acceso y utilidad automáticamente
import lombok.NoArgsConstructor; // Constructor vacío para la serialización JSON de Jackson
import java.time.LocalDateTime; // Tipo para la fecha y hora del check-in en la respuesta

// DTO de SALIDA: datos del check-in enviados al cliente como respuesta HTTP
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para que Jackson serialice a JSON correctamente
@AllArgsConstructor // Constructor completo para construir la respuesta en la capa de servicio
public class CheckInResponseDTO {

    private Long id; // ID único del check-in generado por MySQL, identificador del registro
    private Long reservaId; // ID de la reserva activada con este check-in
    private Long clienteId; // ID del cliente que realizó el check-in
    private Long habitacionId; // ID de la habitación asignada al huésped
    private LocalDateTime fechaHoraCheckIn; // Momento exacto del check-in registrado
    private String observaciones; // Observaciones registradas durante el check-in

}

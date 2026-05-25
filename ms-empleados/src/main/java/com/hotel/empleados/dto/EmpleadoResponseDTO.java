// Paquete de DTOs del microservicio ms-empleados
package com.hotel.empleados.dto;

import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para serialización JSON
import java.math.BigDecimal; // Tipo preciso para el salario en la respuesta
import java.time.LocalDate; // Tipo para la fecha de contratación en la respuesta

// DTO de SALIDA: datos del empleado devueltos al cliente en la respuesta HTTP
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para serialización Jackson a JSON
@AllArgsConstructor // Constructor con todos los campos para construir la respuesta en el Service
public class EmpleadoResponseDTO {

    private Long id; // ID único del empleado generado por MySQL
    private String nombre; // Nombre del empleado en la respuesta
    private String apellido; // Apellido del empleado en la respuesta
    private String cargo; // Cargo del empleado en la respuesta
    private String departamento; // Departamento del empleado en la respuesta
    private String email; // Email del empleado en la respuesta
    private String telefono; // Teléfono del empleado en la respuesta
    private LocalDate fechaContratacion; // Fecha de contratación en la respuesta
    private BigDecimal salario; // Salario del empleado en la respuesta

}

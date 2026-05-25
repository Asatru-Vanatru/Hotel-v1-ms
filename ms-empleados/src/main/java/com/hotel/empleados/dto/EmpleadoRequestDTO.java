// Paquete de DTOs del microservicio ms-empleados
package com.hotel.empleados.dto;

import jakarta.validation.constraints.DecimalMin; // Valida que el salario sea positivo
import jakarta.validation.constraints.Email; // Valida el formato del correo electrónico
import jakarta.validation.constraints.NotBlank; // Valida que los Strings no estén vacíos
import jakarta.validation.constraints.NotNull; // Valida que los campos no sean null
import jakarta.validation.constraints.PastOrPresent; // Valida que la fecha de contratación no sea futura
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.math.BigDecimal; // Tipo exacto para el salario
import java.time.LocalDate; // Tipo para la fecha de contratación

// DTO de ENTRADA para crear o actualizar un empleado del hotel
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío para deserializar el JSON del body
@AllArgsConstructor // Constructor completo para tests y creación directa
public class EmpleadoRequestDTO {

    @NotBlank(message = "El nombre del empleado no puede estar vacío") // Nombre obligatorio
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres") // Límite de la columna MySQL
    private String nombre; // Nombre(s) del empleado

    @NotBlank(message = "El apellido del empleado no puede estar vacío") // Apellido obligatorio
    @Size(max = 80, message = "El apellido no puede superar 80 caracteres") // Límite de la columna MySQL
    private String apellido; // Apellido(s) del empleado

    @NotBlank(message = "El cargo no puede estar vacío") // Cargo obligatorio para asignación
    @Size(max = 60, message = "El cargo no puede superar 60 caracteres") // Límite de la columna MySQL
    private String cargo; // Puesto de trabajo del empleado en el hotel

    @NotBlank(message = "El departamento no puede estar vacío") // Departamento obligatorio
    @Size(max = 30, message = "El departamento no puede superar 30 caracteres") // RECEPCION, COCINA, etc.
    private String departamento; // Área funcional del hotel a la que pertenece el empleado

    @NotBlank(message = "El email no puede estar vacío") // Email obligatorio para comunicaciones
    @Email(message = "El email debe tener un formato válido") // Valida el formato nombre@dominio.com
    @Size(max = 120, message = "El email no puede superar 120 caracteres") // Límite de la columna MySQL
    private String email; // Correo electrónico corporativo del empleado

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres") // Teléfono opcional
    private String telefono; // Número de contacto del empleado

    @NotNull(message = "La fecha de contratación no puede ser nula") // Fecha obligatoria
    @PastOrPresent(message = "La fecha de contratación no puede ser en el futuro") // Solo pasado o presente
    private LocalDate fechaContratacion; // Fecha en que el empleado fue contratado por el hotel

    @NotNull(message = "El salario no puede ser nulo") // Salario obligatorio
    @DecimalMin(value = "0.01", message = "El salario debe ser mayor a 0") // Salario positivo
    private BigDecimal salario; // Remuneración mensual del empleado

}

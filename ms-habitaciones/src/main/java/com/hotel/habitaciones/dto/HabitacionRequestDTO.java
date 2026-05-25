// Paquete de DTOs del microservicio ms-habitaciones
package com.hotel.habitaciones.dto;

// Importa las restricciones de validación de Jakarta Bean Validation
import jakarta.validation.constraints.DecimalMin; // Valida que un número decimal sea mayor a un mínimo
import jakarta.validation.constraints.Min; // Valida que un entero sea mayor o igual a un valor mínimo
import jakarta.validation.constraints.NotBlank; // Valida que un String no sea null, vacío ni solo espacios
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea null
import jakarta.validation.constraints.Size; // Valida la longitud máxima y mínima del texto
import lombok.AllArgsConstructor; // Genera constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y métodos de utilidad
import lombok.NoArgsConstructor; // Genera constructor sin parámetros
import java.math.BigDecimal; // Tipo exacto para el precio por noche

// ═══════════════════════════════════════════════════════════
// HabitacionRequestDTO.java
// DTO de ENTRADA para crear o actualizar una habitación.
// Contiene todas las validaciones de negocio.
// ═══════════════════════════════════════════════════════════

@Data // Genera todos los métodos de acceso y comparación
@NoArgsConstructor // Constructor vacío para deserialización JSON por Jackson
@AllArgsConstructor // Constructor completo para tests y creación directa
public class HabitacionRequestDTO {

    @NotBlank(message = "El número de habitación no puede estar vacío") // Número obligatorio
    @Size(max = 10, message = "El número de habitación no puede superar 10 caracteres") // Limite MySQL
    private String numero; // Número de habitación visible para el huésped

    @NotBlank(message = "El tipo de habitación no puede estar vacío") // Tipo obligatorio
    @Size(max = 30, message = "El tipo no puede superar 30 caracteres") // SIMPLE, DOBLE, SUITE, etc.
    private String tipo; // Categoría de la habitación

    @NotNull(message = "La capacidad no puede ser nula") // La capacidad es obligatoria
    @Min(value = 1, message = "La capacidad debe ser al menos 1 persona") // Mínimo 1 huésped
    private Integer capacidad; // Número máximo de personas en la habitación

    @NotNull(message = "El precio por noche no puede ser nulo") // El precio es obligatorio
    @DecimalMin(value = "0.01", message = "El precio por noche debe ser mayor a 0") // Precio positivo
    private BigDecimal precioNoche; // Costo por cada noche de estadía

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres") // Descripción opcional
    private String descripcion; // Descripción de amenidades e instalaciones

    @NotBlank(message = "El estado de la habitación no puede estar vacío") // Estado obligatorio
    @Size(max = 20, message = "El estado no puede superar 20 caracteres") // DISPONIBLE, OCUPADA, etc.
    private String estado; // Estado actual de la habitación

    @Min(value = 1, message = "El piso debe ser mayor o igual a 1") // El piso debe ser positivo
    private Integer piso; // Número de piso donde está la habitación (opcional)

}

// Paquete de DTOs del microservicio ms-servicios
package com.hotel.servicios.dto;

import jakarta.validation.constraints.DecimalMin; // Valida que el precio sea mayor a 0
import jakarta.validation.constraints.NotBlank; // Valida que el String no esté vacío
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea null
import jakarta.validation.constraints.Size; // Valida la longitud máxima del texto
import lombok.AllArgsConstructor; // Constructor con todos los parámetros
import lombok.Data; // Genera métodos de acceso y utilidad
import lombok.NoArgsConstructor; // Constructor vacío para Jackson
import java.math.BigDecimal; // Tipo exacto para el precio del servicio

// DTO de ENTRADA para crear o actualizar un servicio adicional del catálogo del hotel
@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@NoArgsConstructor // Constructor vacío requerido por Jackson para deserializar el JSON
@AllArgsConstructor // Constructor completo para tests y creación directa
public class ServicioRequestDTO {

    @NotBlank(message = "El nombre del servicio no puede estar vacío") // Nombre obligatorio
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres") // Límite de la columna MySQL
    private String nombre; // Nombre del servicio adicional (ej: "Masaje Relajante", "Room Service")

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres") // Descripción opcional
    private String descripcion; // Descripción detallada de qué incluye el servicio

    @NotNull(message = "El precio del servicio no puede ser nulo") // El precio es obligatorio
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0") // Precio positivo
    private BigDecimal precio; // Precio unitario del servicio en moneda local

    @NotBlank(message = "La categoría del servicio no puede estar vacía") // Categoría obligatoria
    @Size(max = 30, message = "La categoría no puede superar 30 caracteres") // RESTAURANTE, SPA, etc.
    private String categoria; // Categoría a la que pertenece el servicio en el catálogo

    @NotNull(message = "La disponibilidad del servicio no puede ser nula") // Estado obligatorio
    private Boolean disponible; // true si el servicio está activo para solicitar, false si no

}

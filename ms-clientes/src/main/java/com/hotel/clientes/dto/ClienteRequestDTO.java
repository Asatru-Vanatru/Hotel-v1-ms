// Paquete de DTOs del microservicio ms-clientes
package com.hotel.clientes.dto;

// Importa las anotaciones de validación de Jakarta Bean Validation
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // Valida que el campo no sea null ni solo espacios
import jakarta.validation.constraints.Size; // Valida la longitud mínima y máxima del texto
import lombok.AllArgsConstructor; // Genera constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y métodos de utilidad
import lombok.NoArgsConstructor; // Genera constructor sin parámetros

// ═══════════════════════════════════════════════════════════
// ClienteRequestDTO.java
//
// DTO de ENTRADA: objeto que recibe el Controller desde el
// body del POST o PUT. Contiene TODAS las validaciones de
// negocio con anotaciones de Jakarta Validation.
//
// Cuando el Controller usa @Valid sobre este DTO,
// Spring valida automáticamente y devuelve 400 Bad Request
// con los mensajes de error si alguna validación falla.
//
// REGLA: el campo 'id' NO está aquí porque MySQL lo genera.
// ═══════════════════════════════════════════════════════════

@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío para que Jackson deserialice el JSON del body
@AllArgsConstructor // Constructor completo para tests y creación manual
public class ClienteRequestDTO {

    // @NotBlank: rechaza null, string vacío "" y solo espacios "   "
    // @Size: limita la longitud máxima para coincidir con la columna en MySQL
    @NotBlank(message = "El nombre del cliente no puede estar vacío") // Valida que nombre tenga contenido
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres") // Limite de la columna MySQL
    private String nombre; // Nombre(s) del cliente

    @NotBlank(message = "El apellido del cliente no puede estar vacío") // Valida que apellido no esté vacío
    @Size(max = 80, message = "El apellido no puede superar 80 caracteres") // Limite de la columna MySQL
    private String apellido; // Apellido(s) del cliente

    @NotBlank(message = "El email no puede estar vacío") // El email es obligatorio para contacto
    @Email(message = "El email debe tener un formato válido (ejemplo@dominio.com)") // Valida formato email
    @Size(max = 120, message = "El email no puede superar 120 caracteres") // Limite de la columna MySQL
    private String email; // Correo electrónico único del cliente

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres") // Teléfono opcional con límite
    private String telefono; // Número de teléfono de contacto (campo opcional)

    @Size(max = 30, message = "El tipo de documento no puede superar 30 caracteres") // Limite tipo documento
    private String tipoDocumento; // Tipo de documento de identidad (opcional)

    @Size(max = 30, message = "El número de documento no puede superar 30 caracteres") // Limite num documento
    private String numeroDocumento; // Número del documento de identidad (opcional)

    @Size(max = 60, message = "La nacionalidad no puede superar 60 caracteres") // Limite nacionalidad
    private String nacionalidad; // País de origen del cliente (opcional)

}

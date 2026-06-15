// Paquete de la capa web del microservicio ms-clientes
package com.hotel.clientes.controller;

// Importa los DTOs y el servicio necesarios para este controlador
import com.hotel.clientes.dto.ClienteRequestDTO;
import com.hotel.clientes.dto.ClienteResponseDTO;
import com.hotel.clientes.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
// @Valid activa las validaciones del RequestDTO (se integra con Spring Boot Validation)
import jakarta.validation.Valid;
// @RequiredArgsConstructor genera el constructor que inyecta el servicio
import lombok.RequiredArgsConstructor;
// ResponseEntity permite devolver códigos HTTP específicos (200, 201, 204, 404)
import org.springframework.http.ResponseEntity;
// Anotaciones de Spring MVC para mapear rutas y métodos HTTP
import org.springframework.web.bind.annotation.*;

// Importa List para los endpoints que devuelven múltiples recursos
import java.util.List;

// ═══════════════════════════════════════════════════════════
// ClienteController.java
// Capa web RESTful del microservicio ms-clientes.
// Recibe peticiones HTTP, delega al Service y responde JSON.
//
// URL base: http://localhost:8081/api/clientes
// Operaciones: CRUD completo + búsquedas personalizadas
// ═══════════════════════════════════════════════════════════

// @RestController: combina @Controller + @ResponseBody
//   Todos los métodos retornan JSON automáticamente sin @ResponseBody explícito
// @RequestMapping: define la URL base para todos los endpoints de este controller
// @RequiredArgsConstructor: Lombok inyecta el servicio por constructor (sin @Autowired)
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones CRUD y de búsqueda para clientes del hotel")
public class ClienteController {

    // Servicio inyectado por constructor (patrón recomendado por Spring)
    private final ClienteService clienteService;

    // ── GET /api/clientes ────────────────────────────────────────────────────
    // 200 OK con la lista de todos los clientes (puede ser lista vacía [])
    @GetMapping // Mapea las peticiones GET a /api/clientes
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados en el hotel")
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos()); // 200 OK con la lista
    }

    // ── GET /api/clientes/{id} ───────────────────────────────────────────────
    // 200 OK si el cliente existe, 404 Not Found si no existe el id
    @GetMapping("/{id}") // Mapea GET /api/clientes/{id}, extrae el id de la URL
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente específico según su ID")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id) // Busca el cliente por id
                .map(ResponseEntity::ok)                    // Si existe: 200 con el objeto
                .orElse(ResponseEntity.notFound().build()); // Si no existe: 404 sin cuerpo
    }

    // ── POST /api/clientes ───────────────────────────────────────────────────
    // 201 Created con el objeto creado (incluye el id generado por MySQL)
    // @Valid: Spring valida el RequestDTO antes de ejecutar el método
    // Si falla alguna validación: 400 Bad Request automático con errores
    @PostMapping // Mapea las peticiones POST a /api/clientes
    @Operation(summary = "Crear cliente", description = "Crea un nuevo cliente en el sistema")
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(201).body(clienteService.guardar(dto)); // 201 con el recurso creado
    }

    // ── PUT /api/clientes/{id} ───────────────────────────────────────────────
    // 200 OK con el objeto actualizado, 404 si no existe el id
    @PutMapping("/{id}") // Mapea PUT /api/clientes/{id}
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente específico")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Long id, // Extrae el id del path de la URL
            @Valid @RequestBody ClienteRequestDTO dto) { // Valida el body de la petición
        return clienteService.actualizar(id, dto)
                .map(ResponseEntity::ok)                    // Si existe: 200 con datos actualizados
                .orElse(ResponseEntity.notFound().build()); // Si no existe: 404
    }

    // ── DELETE /api/clientes/{id} ────────────────────────────────────────────
    // 204 No Content si se eliminó correctamente, 404 si no existe
    @DeleteMapping("/{id}") // Mapea DELETE /api/clientes/{id}
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente específico del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (clienteService.obtenerPorId(id).isEmpty()) { // Verifica si el cliente existe
            return ResponseEntity.notFound().build(); // 404 si el cliente no existe
        }
        clienteService.eliminar(id); // Elimina el cliente de la base de datos
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // ── ENDPOINTS DE BÚSQUEDA ────────────────────────────────────────────────

    // GET /api/clientes/buscar?nombre=juan → busca por nombre (parcial, sin mayúsculas)
    @GetMapping("/buscar") // Mapea GET /api/clientes/buscar
    @Operation(summary = "Buscar clientes por nombre", description = "Devuelve una lista de clientes cuyo nombre contiene el valor especificado")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(clienteService.buscarPorNombre(nombre)); // 200 con resultados
    }

    // GET /api/clientes/apellido?apellido=garcia → busca por apellido
    @GetMapping("/apellido") // Mapea GET /api/clientes/apellido con parámetro de búsqueda
    @Operation(summary = "Buscar clientes por apellido", description = "Devuelve una lista de clientes cuyo apellido coincide con el valor especificado")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorApellido(@RequestParam String apellido) {
        return ResponseEntity.ok(clienteService.buscarPorApellido(apellido)); // 200 con resultados
    }

    // GET /api/clientes/email?email=juan@mail.com → busca por email exacto
    @GetMapping("/email") // Mapea GET /api/clientes/email con parámetro email
    @Operation(summary = "Buscar cliente por email", description = "Devuelve un cliente específico según su email único")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@RequestParam String email) {
        return clienteService.buscarPorEmail(email) // Busca el cliente por email único
                .map(ResponseEntity::ok)                    // 200 si se encontró
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe ese email
    }

    // GET /api/clientes/nacionalidad/Chilena → busca por nacionalidad (path variable)
    @GetMapping("/nacionalidad/{nacionalidad}") // Mapea GET /api/clientes/nacionalidad/{valor}
    @Operation(summary = "Buscar clientes por nacionalidad", description = "Devuelve una lista de clientes según su nacionalidad") // Documenta este endpoint en Swagger
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNacionalidad(@PathVariable String nacionalidad) {
        return ResponseEntity.ok(clienteService.buscarPorNacionalidad(nacionalidad)); // 200 con lista
    }

    // GET /api/clientes/nombre-completo?nombre=juan garcia → SQL nativo CONCAT
    @GetMapping("/nombre-completo") // Mapea GET /api/clientes/nombre-completo
    @Operation(summary = "Buscar clientes por nombre completo", description = "Devuelve una lista de clientes cuyo nombre completo coincide con el valor especificado") // Documenta este endpoint en Swagger
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNombreCompleto(@RequestParam String nombre) {
        return ResponseEntity.ok(clienteService.buscarPorNombreCompleto(nombre)); // 200 con lista
    }

}

// Paquete de la capa web del microservicio ms-mantenimiento
package com.hotel.mantenimiento.controller;

import com.hotel.mantenimiento.dto.MantenimientoRequestDTO; // DTO de entrada validado
import com.hotel.mantenimiento.dto.MantenimientoResponseDTO; // DTO de salida para la respuesta
import com.hotel.mantenimiento.service.MantenimientoService; // Servicio con la lógica de mantenimiento
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.util.List; // Lista para endpoints con múltiples solicitudes

// Controller REST del microservicio ms-mantenimiento
// URL base: http://localhost:8090/api/mantenimientos
@RestController // Todos los métodos retornan JSON automáticamente
@RequestMapping("/api/mantenimientos") // Ruta base de los endpoints de mantenimiento
@RequiredArgsConstructor // Inyecta el servicio por constructor sin @Autowired
@Tag(name = "Mantenimiento", description = "Endpoints para gestionar el mantenimiento de habitaciones") // Documentación Swagger
public class MantenimientoController {

    private final MantenimientoService mantenimientoService; // Servicio de lógica de mantenimiento

    // GET /api/mantenimientos → 200 con lista de todas las solicitudes
    @GetMapping // Mapea GET a la URL base de mantenimientos
    @Operation(summary = "Obtener todas las solicitudes de mantenimiento", description = "Retorna una lista completa de todas las solicitudes registradas en el sistema") // Documentación Swagger
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(mantenimientoService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/mantenimientos/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/mantenimientos/{id}
    @Operation(summary = "Obtener solicitud por ID", description = "Busca una solicitud de mantenimiento por su ID y retorna los detalles si existe, o un error 404 si no se encuentra") // Documentación Swagger
    public ResponseEntity<MantenimientoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.obtenerPorId(id) // Busca la solicitud por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/mantenimientos → 201 Created con la solicitud registrada
    @PostMapping // Mapea POST para crear una nueva solicitud de mantenimiento
    @Operation(summary = "Crear nueva solicitud de mantenimiento", description = "Registra una nueva solicitud de mantenimiento en el sistema y retorna los detalles de la solicitud creada") // Documentación Swagger
    public ResponseEntity<MantenimientoResponseDTO> crear(@Valid @RequestBody MantenimientoRequestDTO dto) {
        return ResponseEntity.status(201).body(mantenimientoService.guardar(dto)); // 201 con solicitud creada
    }

    // PUT /api/mantenimientos/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/mantenimientos/{id} (ej: cambiar estado a COMPLETADO)
    @Operation(summary = "Actualizar solicitud de mantenimiento", description = "Actualiza los detalles de una solicitud de mantenimiento existente y retorna los nuevos detalles") // Documentación Swagger
    public ResponseEntity<MantenimientoResponseDTO> actualizar(
            @PathVariable Long id, // ID de la solicitud a actualizar
            @Valid @RequestBody MantenimientoRequestDTO dto) { // Body validado
        return mantenimientoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/mantenimientos/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/mantenimientos/{id}
    @Operation(summary = "Eliminar solicitud de mantenimiento", description = "Elimina una solicitud de mantenimiento existente del sistema y retorna un código 204 si la eliminación fue exitosa o un error 404 si la solicitud no se encuentra") // Documentación Swagger
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (mantenimientoService.obtenerPorId(id).isEmpty()) { // Verifica existencia
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        mantenimientoService.eliminar(id); // Elimina la solicitud de mantenimiento
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/mantenimientos/estado/PENDIENTE → filtra por estado de la solicitud
    @GetMapping("/estado/{estado}") // Mapea GET /api/mantenimientos/estado/{valor}
    @Operation(summary = "Filtrar solicitudes por estado", description = "Retorna una lista de solicitudes de mantenimiento que coinciden con el estado especificado, como PENDIENTE, EN_PROGRESO o COMPLETADO") // Documentación Swagger
    public ResponseEntity<List<MantenimientoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(mantenimientoService.buscarPorEstado(estado)); // 200 filtrado
    }

    // GET /api/mantenimientos/habitacion/{habitacionId} → solicitudes de una habitación
    @GetMapping("/habitacion/{habitacionId}") // Mapea GET /api/mantenimientos/habitacion/{id}
    @Operation(summary = "Filtrar solicitudes por habitación", description = "Retorna una lista de solicitudes de mantenimiento asociadas a una habitación específica") // Documentación Swagger
    public ResponseEntity<List<MantenimientoResponseDTO>> buscarPorHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(mantenimientoService.buscarPorHabitacion(habitacionId)); // 200 con lista
    }

    // GET /api/mantenimientos/estadisticas/urgentes → conteo de urgentes (JPQL con IN)
    @GetMapping("/estadisticas/urgentes") // Mapea GET para el conteo de urgencias
    @Operation(summary = "Contar solicitudes urgentes", description = "Retorna el número total de solicitudes de mantenimiento que están marcadas como urgentes") // Documentación Swagger
    public ResponseEntity<Long> contarUrgentes() {
        return ResponseEntity.ok(mantenimientoService.contarPendientesUrgentes()); // 200 con JPQL
    }

    // GET /api/mantenimientos/estadisticas/habitaciones-problematicas → top 5 (SQL nativo)
    @GetMapping("/estadisticas/habitaciones-problematicas") // Mapea GET para el ranking
    @Operation(summary = "Obtener habitaciones más problemáticas", description = "Retorna una lista de las 5 habitaciones con más solicitudes de mantenimiento") // Documentación Swagger
    public ResponseEntity<List<Object[]>> obtenerHabitacionesProblematicas() {
        return ResponseEntity.ok(mantenimientoService.obtenerHabitacionesMasProblematicas()); // 200 SQL nativo
    }

}

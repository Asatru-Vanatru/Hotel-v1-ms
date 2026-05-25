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
public class MantenimientoController {

    private final MantenimientoService mantenimientoService; // Servicio de lógica de mantenimiento

    // GET /api/mantenimientos → 200 con lista de todas las solicitudes
    @GetMapping // Mapea GET a la URL base de mantenimientos
    public ResponseEntity<List<MantenimientoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(mantenimientoService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/mantenimientos/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/mantenimientos/{id}
    public ResponseEntity<MantenimientoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.obtenerPorId(id) // Busca la solicitud por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/mantenimientos → 201 Created con la solicitud registrada
    @PostMapping // Mapea POST para crear una nueva solicitud de mantenimiento
    public ResponseEntity<MantenimientoResponseDTO> crear(@Valid @RequestBody MantenimientoRequestDTO dto) {
        return ResponseEntity.status(201).body(mantenimientoService.guardar(dto)); // 201 con solicitud creada
    }

    // PUT /api/mantenimientos/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/mantenimientos/{id} (ej: cambiar estado a COMPLETADO)
    public ResponseEntity<MantenimientoResponseDTO> actualizar(
            @PathVariable Long id, // ID de la solicitud a actualizar
            @Valid @RequestBody MantenimientoRequestDTO dto) { // Body validado
        return mantenimientoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/mantenimientos/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/mantenimientos/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (mantenimientoService.obtenerPorId(id).isEmpty()) { // Verifica existencia
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        mantenimientoService.eliminar(id); // Elimina la solicitud de mantenimiento
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/mantenimientos/estado/PENDIENTE → filtra por estado de la solicitud
    @GetMapping("/estado/{estado}") // Mapea GET /api/mantenimientos/estado/{valor}
    public ResponseEntity<List<MantenimientoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(mantenimientoService.buscarPorEstado(estado)); // 200 filtrado
    }

    // GET /api/mantenimientos/habitacion/{habitacionId} → solicitudes de una habitación
    @GetMapping("/habitacion/{habitacionId}") // Mapea GET /api/mantenimientos/habitacion/{id}
    public ResponseEntity<List<MantenimientoResponseDTO>> buscarPorHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(mantenimientoService.buscarPorHabitacion(habitacionId)); // 200 con lista
    }

    // GET /api/mantenimientos/estadisticas/urgentes → conteo de urgentes (JPQL con IN)
    @GetMapping("/estadisticas/urgentes") // Mapea GET para el conteo de urgencias
    public ResponseEntity<Long> contarUrgentes() {
        return ResponseEntity.ok(mantenimientoService.contarPendientesUrgentes()); // 200 con JPQL
    }

    // GET /api/mantenimientos/estadisticas/habitaciones-problematicas → top 5 (SQL nativo)
    @GetMapping("/estadisticas/habitaciones-problematicas") // Mapea GET para el ranking
    public ResponseEntity<List<Object[]>> obtenerHabitacionesProblematicas() {
        return ResponseEntity.ok(mantenimientoService.obtenerHabitacionesMasProblematicas()); // 200 SQL nativo
    }

}

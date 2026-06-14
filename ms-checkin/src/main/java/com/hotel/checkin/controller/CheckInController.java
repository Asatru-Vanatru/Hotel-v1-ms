// Paquete de la capa web del microservicio ms-checkin
package com.hotel.checkin.controller;

import com.hotel.checkin.dto.CheckInRequestDTO; // DTO de entrada validado para el body
import com.hotel.checkin.dto.CheckInResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.checkin.service.CheckInService; // Servicio con la lógica de check-in
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.util.List; // Lista para endpoints con múltiples check-ins

// Controller REST del microservicio ms-checkin
// URL base: http://localhost:8084/api/checkins
@RestController // Todos los métodos retornan JSON sin @ResponseBody explícito
@RequestMapping("/api/checkins") // Ruta raíz de los endpoints de check-in
@RequiredArgsConstructor // Inyección del servicio por constructor (sin @Autowired)
@Tag(name = "Check-in", description = "Endpoints para gestionar el proceso de check-in en el hotel") // Documentación OpenAPI
public class CheckInController {

    private final CheckInService checkInService; // Servicio de lógica del proceso de check-in

    // GET /api/checkins → 200 con lista de todos los check-ins registrados
    @GetMapping // Mapea GET a la URL base /api/checkins
    @Operation(summary = "Obtener todos los check-ins", description = "Retorna una lista de todos los check-ins registrados en el sistema") // Documentación OpenAPI
    public ResponseEntity<List<CheckInResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(checkInService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/checkins/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/checkins/{id}
    @Operation(summary = "Obtener un check-in por ID", description = "Retorna los detalles de un check-in específico por su ID") // Documentación OpenAPI
    public ResponseEntity<CheckInResponseDTO> obtenerPorId(@PathVariable Long id) {
        return checkInService.obtenerPorId(id) // Busca el check-in por id
                .map(ResponseEntity::ok)                    // 200 con los datos del check-in
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/checkins → 201 Created con el registro de check-in creado
    @PostMapping // Mapea POST a /api/checkins para registrar un nuevo check-in
    @Operation(summary = "Registrar un nuevo check-in", description = "Crea un nuevo registro de check-in en el sistema") // Documentación OpenAPI
    public ResponseEntity<CheckInResponseDTO> registrar(@Valid @RequestBody CheckInRequestDTO dto) {
        return ResponseEntity.status(201).body(checkInService.registrar(dto)); // 201 con registro creado
    }

    // PUT /api/checkins/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/checkins/{id}
    @Operation(summary = "Actualizar un check-in", description = "Actualiza los datos de un check-in específico por su ID") // Documentación OpenAPI
    public ResponseEntity<CheckInResponseDTO> actualizar(
            @PathVariable Long id, // ID del check-in a actualizar
            @Valid @RequestBody CheckInRequestDTO dto) { // Body validado
        return checkInService.actualizar(id, dto)
                .map(ResponseEntity::ok)                    // 200 con datos actualizados
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/checkins/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/checkins/{id}
    @Operation(summary = "Eliminar un check-in", description = "Elimina un check-in específico por su ID") // Documentación OpenAPI
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (checkInService.obtenerPorId(id).isEmpty()) { // Verifica si el check-in existe
            return ResponseEntity.notFound().build(); // 404 si no se encontró
        }
        checkInService.eliminar(id); // Elimina el registro de check-in
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/checkins/reserva/{reservaId} → check-in de una reserva específica
    @GetMapping("/reserva/{reservaId}") // Mapea GET /api/checkins/reserva/{id}
    @Operation(summary = "Buscar check-in por reserva", description = "Retorna el check-in asociado a una reserva específica") // Documentación OpenAPI
    public ResponseEntity<CheckInResponseDTO> buscarPorReserva(@PathVariable Long reservaId) {
        return checkInService.buscarPorReserva(reservaId) // Busca por id de reserva
                .map(ResponseEntity::ok)                    // 200 si existe
                .orElse(ResponseEntity.notFound().build()); // 404 si no hay check-in para esa reserva
    }

    // GET /api/checkins/cliente/{clienteId} → historial de check-ins del cliente
    @GetMapping("/cliente/{clienteId}") // Mapea GET /api/checkins/cliente/{id}
    @Operation(summary = "Buscar check-ins por cliente", description = "Retorna el historial de check-ins asociados a un cliente específico") // Documentación OpenAPI
    public ResponseEntity<List<CheckInResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(checkInService.buscarPorCliente(clienteId)); // 200 con historial
    }

    // GET /api/checkins/hoy → check-ins registrados el día de hoy (JPQL)
    @GetMapping("/hoy") // Mapea GET /api/checkins/hoy para el reporte del día
    @Operation(summary = "Obtener check-ins del día", description = "Retorna la lista de check-ins registrados el día de hoy") // Documentación OpenAPI
    public ResponseEntity<List<CheckInResponseDTO>> obtenerDeHoy() {
        return ResponseEntity.ok(checkInService.obtenerCheckInsDeHoy()); // 200 con lista del día
    }

    // GET /api/checkins/estadisticas/ultimo-mes → conteo del último mes (SQL nativo)
    @GetMapping("/estadisticas/ultimo-mes") // Mapea GET para estadísticas del último mes
    @Operation(summary = "Contar check-ins del último mes", description = "Retorna el número de check-ins registrados en el último mes") // Documentación OpenAPI
    public ResponseEntity<Long> contarUltimoMes() {
        return ResponseEntity.ok(checkInService.contarCheckInsUltimoMes()); // 200 con conteo
    }

}

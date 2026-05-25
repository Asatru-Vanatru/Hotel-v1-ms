// Paquete de la capa web del microservicio ms-consumos
package com.hotel.consumos.controller;

import com.hotel.consumos.dto.ConsumoRequestDTO; // DTO de entrada validado para el body
import com.hotel.consumos.dto.ConsumoResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.consumos.service.ConsumoService; // Servicio con la lógica de consumos
import jakarta.validation.Valid; // Activa la validación del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.math.BigDecimal; // Tipo para el total de consumos calculado
import java.util.List; // Lista para endpoints con múltiples consumos

// Controller REST del microservicio ms-consumos
// URL base: http://localhost:8087/api/consumos
@RestController // Todos los métodos retornan JSON automáticamente
@RequestMapping("/api/consumos") // Ruta base de los endpoints de consumos de servicios
@RequiredArgsConstructor // Inyecta el servicio por constructor sin @Autowired
public class ConsumoController {

    private final ConsumoService consumoService; // Servicio de lógica de consumos

    // GET /api/consumos → 200 con lista de todos los consumos registrados
    @GetMapping // Mapea GET a la URL base de consumos
    public ResponseEntity<List<ConsumoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(consumoService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/consumos/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/consumos/{id}
    public ResponseEntity<ConsumoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return consumoService.obtenerPorId(id) // Busca el consumo por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/consumos → 201 Created con el consumo registrado
    @PostMapping // Mapea POST para registrar un nuevo consumo de servicio
    public ResponseEntity<ConsumoResponseDTO> registrar(@Valid @RequestBody ConsumoRequestDTO dto) {
        return ResponseEntity.status(201).body(consumoService.registrar(dto)); // 201 con registro
    }

    // PUT /api/consumos/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/consumos/{id}
    public ResponseEntity<ConsumoResponseDTO> actualizar(
            @PathVariable Long id, // ID del consumo a actualizar
            @Valid @RequestBody ConsumoRequestDTO dto) { // Body validado
        return consumoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/consumos/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/consumos/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (consumoService.obtenerPorId(id).isEmpty()) { // Verifica si el consumo existe
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        consumoService.eliminar(id); // Elimina el consumo de la base de datos
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }

    // GET /api/consumos/reserva/{reservaId} → consumos de una reserva específica
    @GetMapping("/reserva/{reservaId}") // Mapea GET /api/consumos/reserva/{id}
    public ResponseEntity<List<ConsumoResponseDTO>> buscarPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(consumoService.buscarPorReserva(reservaId)); // 200 con lista
    }

    // GET /api/consumos/reserva/{reservaId}/total → total de consumos de la reserva (JPQL)
    @GetMapping("/reserva/{reservaId}/total") // Mapea GET para el total de una reserva
    public ResponseEntity<BigDecimal> calcularTotalReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(consumoService.calcularTotalPorReserva(reservaId)); // 200 con JPQL SUM
    }

    // GET /api/consumos/estadisticas/mas-consumidos → top 5 servicios (SQL nativo)
    @GetMapping("/estadisticas/mas-consumidos") // Mapea GET para el ranking de servicios
    public ResponseEntity<List<Object[]>> obtenerMasConsumidos() {
        return ResponseEntity.ok(consumoService.obtenerServiciosMasConsumidos()); // 200 con SQL nativo
    }

}

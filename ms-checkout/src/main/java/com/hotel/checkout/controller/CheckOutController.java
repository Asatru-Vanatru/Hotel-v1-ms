// Paquete de la capa web del microservicio ms-checkout
package com.hotel.checkout.controller;

import com.hotel.checkout.dto.CheckOutRequestDTO; // DTO de entrada validado
import com.hotel.checkout.dto.CheckOutResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.checkout.service.CheckOutService; // Servicio con la lógica de check-out
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO con @Valid
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Permite devolver códigos HTTP específicos
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.math.BigDecimal; // Tipo para el total de ingresos en el endpoint de estadísticas
import java.util.List; // Lista para endpoints que devuelven múltiples check-outs

// Controller REST del microservicio ms-checkout
// URL base: http://localhost:8085/api/checkouts
@RestController // Todos los métodos retornan JSON automáticamente
@RequestMapping("/api/checkouts") // Ruta base de todos los endpoints de check-out
@RequiredArgsConstructor // Inyecta el servicio por constructor
public class CheckOutController {

    private final CheckOutService checkOutService; // Servicio de lógica del proceso de check-out

    // GET /api/checkouts → 200 con lista de todos los check-outs
    @GetMapping // Mapea GET a la URL base
    public ResponseEntity<List<CheckOutResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(checkOutService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/checkouts/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/checkouts/{id}
    public ResponseEntity<CheckOutResponseDTO> obtenerPorId(@PathVariable Long id) {
        return checkOutService.obtenerPorId(id) // Busca el check-out por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/checkouts → 201 Created con el registro de check-out
    @PostMapping // Mapea POST para registrar un nuevo check-out
    public ResponseEntity<CheckOutResponseDTO> registrar(@Valid @RequestBody CheckOutRequestDTO dto) {
        return ResponseEntity.status(201).body(checkOutService.registrar(dto)); // 201 con registro
    }

    // PUT /api/checkouts/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/checkouts/{id}
    public ResponseEntity<CheckOutResponseDTO> actualizar(
            @PathVariable Long id, // ID del check-out a actualizar
            @Valid @RequestBody CheckOutRequestDTO dto) { // Body validado
        return checkOutService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/checkouts/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/checkouts/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (checkOutService.obtenerPorId(id).isEmpty()) { // Verifica existencia
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        checkOutService.eliminar(id); // Elimina el check-out
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }

    // GET /api/checkouts/reserva/{reservaId} → check-out de una reserva específica
    @GetMapping("/reserva/{reservaId}") // Mapea GET /api/checkouts/reserva/{id}
    public ResponseEntity<CheckOutResponseDTO> buscarPorReserva(@PathVariable Long reservaId) {
        return checkOutService.buscarPorReserva(reservaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no hay check-out de esa reserva
    }

    // GET /api/checkouts/estadisticas/ingresos → total de ingresos acumulados (JPQL)
    @GetMapping("/estadisticas/ingresos") // Mapea GET para el total de ingresos
    public ResponseEntity<BigDecimal> obtenerIngresosTotales() {
        return ResponseEntity.ok(checkOutService.calcularIngresosTotales()); // 200 con total JPQL
    }

    // GET /api/checkouts/estadisticas/promedio-estancia → días promedio (SQL nativo)
    @GetMapping("/estadisticas/promedio-estancia") // Mapea GET para el promedio de estadía
    public ResponseEntity<Double> obtenerPromedioEstancia() {
        return ResponseEntity.ok(checkOutService.calcularPromedioEstancia()); // 200 con AVG SQL nativo
    }

}

// Paquete de la capa web del microservicio ms-reservas
package com.hotel.reservas.controller;

import com.hotel.reservas.dto.ReservaRequestDTO; // DTO de entrada validado para el body
import com.hotel.reservas.dto.ReservaResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.reservas.service.ReservaService; // Servicio con la lógica de negocio
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.http.ResponseEntity; // Controla el código HTTP de la respuesta
import org.springframework.web.bind.annotation.*; // Todas las anotaciones REST de Spring MVC
import java.util.List; // Lista para endpoints con múltiples reservas

// Controller REST del microservicio ms-reservas
// URL base: http://localhost:8083/api/reservas
@RestController // Todos los métodos devuelven JSON automáticamente
@RequestMapping("/api/reservas") // Ruta raíz de todos los endpoints de reservas
@RequiredArgsConstructor // Inyección por constructor del servicio
public class ReservaController {

    private final ReservaService reservaService; // Servicio de lógica de negocio de reservas

    // GET /api/reservas → 200 con lista completa de reservas
    @GetMapping // Mapea GET sin ruta adicional
    public ResponseEntity<List<ReservaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas()); // 200 con lista completa
    }

    // GET /api/reservas/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/reservas/{id}
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id) // Busca la reserva por id
                .map(ResponseEntity::ok)                    // 200 con los datos de la reserva
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/reservas → 201 Created con la reserva creada (total calculado)
    @PostMapping // Mapea POST a la URL base de reservas
    public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.status(201).body(reservaService.guardar(dto)); // 201 con total calculado
    }

    // PUT /api/reservas/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/reservas/{id}
    public ResponseEntity<ReservaResponseDTO> actualizar(
            @PathVariable Long id, // ID de la reserva a actualizar
            @Valid @RequestBody ReservaRequestDTO dto) { // Body validado con @Valid
        return reservaService.actualizar(id, dto)
                .map(ResponseEntity::ok)                    // 200 con datos actualizados
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/reservas/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/reservas/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) { // Verifica si la reserva existe
            return ResponseEntity.notFound().build(); // 404 si no existe la reserva
        }
        reservaService.eliminar(id); // Cancela y elimina la reserva de la base de datos
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }

    // GET /api/reservas/cliente/{clienteId} → historial de reservas de un cliente
    @GetMapping("/cliente/{clienteId}") // Mapea GET /api/reservas/cliente/{id}
    public ResponseEntity<List<ReservaResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.buscarPorCliente(clienteId)); // 200 con historial
    }

    // GET /api/reservas/estado/CONFIRMADA → filtra reservas por estado
    @GetMapping("/estado/{estado}") // Mapea GET /api/reservas/estado/{valor}
    public ResponseEntity<List<ReservaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(reservaService.buscarPorEstado(estado)); // 200 con lista filtrada
    }

    // GET /api/reservas/proximas-llegadas → próximas reservas confirmadas (JPQL ORDER BY)
    @GetMapping("/proximas-llegadas") // Mapea GET /api/reservas/proximas-llegadas
    public ResponseEntity<List<ReservaResponseDTO>> obtenerProximasLlegadas() {
        return ResponseEntity.ok(reservaService.obtenerProximasLlegadas()); // 200 con JPQL
    }

    // GET /api/reservas/cliente/{clienteId}/noches → total noches del cliente (SQL nativo)
    @GetMapping("/cliente/{clienteId}/noches") // Mapea GET para calcular noches del cliente
    public ResponseEntity<Long> calcularNochesCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.calcularNochesCliente(clienteId)); // 200 con total noches
    }

}

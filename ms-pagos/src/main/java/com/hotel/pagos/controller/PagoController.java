// Paquete de la capa web del microservicio ms-pagos
package com.hotel.pagos.controller;

import com.hotel.pagos.dto.PagoRequestDTO; // DTO de entrada validado para el body
import com.hotel.pagos.dto.PagoResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.pagos.service.PagoService; // Servicio con la lógica de pagos
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.math.BigDecimal; // Tipo para el total pagado calculado en el endpoint
import java.util.List; // Lista para endpoints con múltiples pagos

// Controller REST del microservicio ms-pagos
// URL base: http://localhost:8088/api/pagos
@RestController // Todos los métodos retornan JSON sin configuración adicional
@RequestMapping("/api/pagos") // Ruta base de todos los endpoints de pagos
@RequiredArgsConstructor // Inyecta el servicio por constructor sin @Autowired
public class PagoController {

    private final PagoService pagoService; // Servicio de lógica de procesamiento de pagos

    // GET /api/pagos → 200 con lista de todos los registros de pago
    @GetMapping // Mapea GET a la URL base de pagos
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/pagos/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/pagos/{id}
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id) // Busca el pago por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/pagos → 201 Created con el pago registrado
    @PostMapping // Mapea POST para registrar una transacción de pago
    public ResponseEntity<PagoResponseDTO> registrar(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(201).body(pagoService.registrar(dto)); // 201 con pago creado
    }

    // PUT /api/pagos/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/pagos/{id} para actualizar (ej: cambiar estado)
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id, // ID del pago a actualizar
            @Valid @RequestBody PagoRequestDTO dto) { // Body validado
        return pagoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/pagos/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/pagos/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pagoService.obtenerPorId(id).isEmpty()) { // Verifica si el pago existe
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        pagoService.eliminar(id); // Elimina el registro de pago
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/pagos/reserva/{reservaId} → pagos de una reserva específica
    @GetMapping("/reserva/{reservaId}") // Mapea GET /api/pagos/reserva/{id}
    public ResponseEntity<List<PagoResponseDTO>> buscarPorReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.buscarPorReserva(reservaId)); // 200 con pagos de la reserva
    }

    // GET /api/pagos/estado/COMPLETADO → filtra por estado del pago
    @GetMapping("/estado/{estado}") // Mapea GET /api/pagos/estado/{valor}
    public ResponseEntity<List<PagoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.buscarPorEstado(estado)); // 200 filtrado por estado
    }

    // GET /api/pagos/reserva/{reservaId}/total → total pagado de una reserva (JPQL)
    @GetMapping("/reserva/{reservaId}/total") // Mapea GET para calcular total de la reserva
    public ResponseEntity<BigDecimal> calcularTotalReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.calcularTotalPorReserva(reservaId)); // 200 con JPQL SUM
    }

    // GET /api/pagos/estadisticas/por-metodo → ingresos por método de pago (SQL nativo)
    @GetMapping("/estadisticas/por-metodo") // Mapea GET para el reporte por método de pago
    public ResponseEntity<List<Object[]>> obtenerIngresosPorMetodo() {
        return ResponseEntity.ok(pagoService.obtenerIngresosPorMetodo()); // 200 con SQL nativo GROUP BY
    }

}

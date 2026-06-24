package com.hotel.reservas.controller;

import com.hotel.reservas.dto.ReservaRequestDTO;
import com.hotel.reservas.dto.ReservaResponseDTO;
import com.hotel.reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/reservas")
@Tag(name = "Reservas V2", description = "Endpoints para gestionar reservas del hotel (versión 2)")
public class ReservaControllerV2 {

    private final ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Obtener todas las reservas")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva reserva")
    public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.status(201).body(reservaService.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva existente")
    public ResponseEntity<ReservaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDTO dto) {
        return reservaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Historial de reservas por cliente")
    public ResponseEntity<List<ReservaResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.buscarPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Filtrar reservas por estado")
    public ResponseEntity<List<ReservaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(reservaService.buscarPorEstado(estado));
    }

    @GetMapping("/proximas-llegadas")
    @Operation(summary = "Próximas llegadas confirmadas")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerProximasLlegadas() {
        return ResponseEntity.ok(reservaService.obtenerProximasLlegadas());
    }

    @GetMapping("/cliente/{clienteId}/noches")
    @Operation(summary = "Calcular total noches por cliente")
    public ResponseEntity<Long> calcularNochesCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.calcularNochesCliente(clienteId));
    }
}

package com.hotel.reservas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel.reservas.assemblers.ReservaModelAssembler;
import com.hotel.reservas.dto.ReservaRequestDTO;
import com.hotel.reservas.dto.ReservaResponseDTO;
import com.hotel.reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/reservas")
@Tag(name = "Reservas V2", description = "Endpoints para gestionar reservas del hotel (versión 2)")
public class ReservaControllerV2 {

    private final ReservaService reservaService;

    private final ReservaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las reservas", description = "Devuelve una lista completa de todas las reservas en formato HAL+JSON")
    public CollectionModel<EntityModel<ReservaResponseDTO>> obtenerTodas() {
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener reserva por ID", description = "Devuelve los detalles de una reserva específica en formato HAL+JSON")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear nueva reserva", description = "Crea una nueva reserva y devuelve la reserva creada con el total calculado en formato HAL+JSON")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> crearReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO nuevaReserva = reservaService.guardar(dto);
        return ResponseEntity
                .created(linkTo(methodOn(ReservaControllerV2.class).obtenerPorId(nuevaReserva.getId())).toUri())
                .body(assembler.toModel(nuevaReserva));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar reserva existente", description = "Actualiza los datos de una reserva existente y devuelve la reserva actualizada en formato HAL+JSON")
    public ResponseEntity<EntityModel<ReservaResponseDTO>> actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO dto) {
        return reservaService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar reserva", description = "Cancela y elimina una reserva existente por su ID")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        if (reservaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/cliente/{clienteId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Historial de reservas por cliente", description = "Devuelve el historial completo de reservas de un cliente en formato HAL+JSON")
    public CollectionModel<EntityModel<ReservaResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.buscarPorCliente(clienteId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).buscarPorCliente(clienteId)).withSelfRel()
        );
    }

    @GetMapping(value = "/estado/{estado}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Filtrar reservas por estado", description = "Devuelve una lista de reservas filtradas por estado en formato HAL+JSON")
    public CollectionModel<EntityModel<ReservaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.buscarPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).buscarPorEstado(estado)).withSelfRel()
        );
    }

    @GetMapping(value = "/proximas-llegadas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Próximas llegadas confirmadas", description = "Devuelve las próximas reservas confirmadas ordenadas por fecha de llegada en formato HAL+JSON")
    public CollectionModel<EntityModel<ReservaResponseDTO>> obtenerProximasLlegadas() {
        List<EntityModel<ReservaResponseDTO>> reservas = reservaService.obtenerProximasLlegadas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).obtenerProximasLlegadas()).withSelfRel()
        );
    }

    @GetMapping(value = "/cliente/{clienteId}/noches", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Calcular total noches por cliente", description = "Devuelve el total de noches reservadas por un cliente utilizando una consulta SQL nativa")
    public ResponseEntity<Long> calcularNochesCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.calcularNochesCliente(clienteId));
    }

}

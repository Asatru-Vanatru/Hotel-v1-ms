package com.hotel.checkin.controller;

import com.hotel.checkin.assemblers.CheckInModelAssemblers;
import com.hotel.checkin.dto.CheckInRequestDTO;
import com.hotel.checkin.model.CheckIn;
import com.hotel.checkin.service.CheckInService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/checkins")
public class CheckInControllerV2 {

    private final CheckInService checkInService;
    private final CheckInModelAssemblers assembler;

    public CheckInControllerV2(CheckInService checkInService, CheckInModelAssemblers assembler) {
        this.checkInService = checkInService;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckIn>> obtenerTodos() {
        List<EntityModel<CheckIn>> checkIns = checkInService.obtenerTodosEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkIns,
                linkTo(methodOn(CheckInControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckIn>> obtenerPorId(@PathVariable Long id) {
        try {
            CheckIn checkIn = checkInService.obtenerEntidadPorId(id);
            return ResponseEntity.ok(assembler.toModel(checkIn));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckIn>> registrar(@Valid @RequestBody CheckInRequestDTO dto) {
        CheckIn checkIn = new CheckIn(null, dto.getReservaId(), dto.getClienteId(),
                dto.getHabitacionId(), dto.getFechaHoraCheckIn(), dto.getObservaciones());
        CheckIn nuevo = checkInService.guardar(checkIn);
        return ResponseEntity
                .created(linkTo(methodOn(CheckInControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckIn>> actualizar(@PathVariable Long id, @Valid @RequestBody CheckInRequestDTO dto) {
        try {
            checkInService.obtenerEntidadPorId(id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        CheckIn checkIn = new CheckIn(id, dto.getReservaId(), dto.getClienteId(),
                dto.getHabitacionId(), dto.getFechaHoraCheckIn(), dto.getObservaciones());
        CheckIn actualizado = checkInService.guardar(checkIn);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            checkInService.obtenerEntidadPorId(id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        checkInService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ── MÉTODOS PERSONALIZADOS ───────────────────────────────────────────────

    // 1. Check-ins de un cliente en una fecha específica
    // GET /api/v2/checkins/cliente/{clienteId}/fecha?fecha=2024-01-15
    @GetMapping(value = "/cliente/{clienteId}/fecha", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckIn>> getCheckInsByClienteYFecha(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<EntityModel<CheckIn>> checkIns = checkInService.findCheckInsByClienteYFecha(clienteId, fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkIns,
                linkTo(methodOn(CheckInControllerV2.class).getCheckInsByClienteYFecha(clienteId, fecha)).withSelfRel());
    }

    // 2. Check-ins de una habitación por un cliente específico
    // GET /api/v2/checkins/habitacion/{habitacionId}/cliente/{clienteId}
    @GetMapping(value = "/habitacion/{habitacionId}/cliente/{clienteId}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckIn>> getCheckInsByHabitacionYCliente(
            @PathVariable Long habitacionId,
            @PathVariable Long clienteId) {
        List<EntityModel<CheckIn>> checkIns = checkInService.findCheckInsByHabitacionYCliente(habitacionId, clienteId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkIns,
                linkTo(methodOn(CheckInControllerV2.class).getCheckInsByHabitacionYCliente(habitacionId, clienteId)).withSelfRel());
    }

    // 3. Check-ins de un cliente entre dos fechas
    // GET /api/v2/checkins/cliente/{clienteId}/fechas?inicio=2024-01-01T00:00:00&fin=2024-01-31T23:59:59
    @GetMapping(value = "/cliente/{clienteId}/fechas", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckIn>> getCheckInsByClienteEntreFechas(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<EntityModel<CheckIn>> checkIns = checkInService.findCheckInsByClienteEntreFechas(clienteId, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkIns,
                linkTo(methodOn(CheckInControllerV2.class).getCheckInsByClienteEntreFechas(clienteId, inicio, fin)).withSelfRel());
    }

    // 4. Check-ins de una habitación entre dos fechas
    // GET /api/v2/checkins/habitacion/{habitacionId}/fechas?inicio=2024-01-01T00:00:00&fin=2024-01-31T23:59:59
    @GetMapping(value = "/habitacion/{habitacionId}/fechas", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckIn>> getCheckInsByHabitacionEntreFechas(
            @PathVariable Long habitacionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<EntityModel<CheckIn>> checkIns = checkInService.findCheckInsByHabitacionEntreFechas(habitacionId, inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkIns,
                linkTo(methodOn(CheckInControllerV2.class).getCheckInsByHabitacionEntreFechas(habitacionId, inicio, fin)).withSelfRel());
    }

    // 5. Total de check-ins realizados en una habitación específica
    // GET /api/v2/checkins/habitacion/{habitacionId}/total
    @GetMapping(value = "/habitacion/{habitacionId}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> contarCheckInsPorHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(checkInService.contarCheckInsPorHabitacion(habitacionId));
    }

}

package com.hotel.reservas.controller;

import com.hotel.reservas.assemblers.ReservaModelAssembler;
import com.hotel.reservas.model.Reserva;
import com.hotel.reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "API HATEOAS V2 para reservas")
public class ReservaControllerV2 {

    private final ReservaService reservaService;
    private final ReservaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Reserva>> obtenerTodas() {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reserva>> obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reserva>> crear(@RequestBody Reserva reserva) {
        Reserva nuevo = reservaService.guardarEntidad(reserva);
        return ResponseEntity
                .created(linkTo(methodOn(ReservaControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Reserva>> actualizar(@PathVariable Long id, @RequestBody Reserva reserva) {
        return reservaService.obtenerEntidadPorId(id)
                .map(existing -> {
                    reserva.setId(id);
                    Reserva actualizado = reservaService.guardarEntidad(reserva);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reservaService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservaService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

package com.hotel.habitaciones.controller;

import com.hotel.habitaciones.assemblers.HabitacionModelAssembler;
import com.hotel.habitaciones.model.Habitacion;
import com.hotel.habitaciones.service.HabitacionService;
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
@RequestMapping("/api/v2/habitaciones")
@RequiredArgsConstructor
@Tag(name = "Habitaciones", description = "API HATEOAS V2 para habitaciones")
public class HabitacionControllerV2 {

    private final HabitacionService habitacionService;
    private final HabitacionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Habitacion>> obtenerTodas() {
        List<EntityModel<Habitacion>> habitaciones = habitacionService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(habitaciones,
                linkTo(methodOn(HabitacionControllerV2.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Habitacion>> obtenerPorId(@PathVariable Long id) {
        return habitacionService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Habitacion>> crear(@RequestBody Habitacion habitacion) {
        Habitacion nuevo = habitacionService.guardarEntidad(habitacion);
        return ResponseEntity
                .created(linkTo(methodOn(HabitacionControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Habitacion>> actualizar(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        return habitacionService.obtenerEntidadPorId(id)
                .map(existing -> {
                    habitacion.setId(id);
                    Habitacion actualizado = habitacionService.guardarEntidad(habitacion);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (habitacionService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        habitacionService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

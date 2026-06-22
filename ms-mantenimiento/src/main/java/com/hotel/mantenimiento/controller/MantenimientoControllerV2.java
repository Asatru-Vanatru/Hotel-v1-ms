package com.hotel.mantenimiento.controller;

import com.hotel.mantenimiento.assemblers.MantenimientoModelAssembler;
import com.hotel.mantenimiento.model.Mantenimiento;
import com.hotel.mantenimiento.service.MantenimientoService;
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
@RequestMapping("/api/v2/mantenimientos")
@RequiredArgsConstructor
@Tag(name = "Mantenimientos", description = "API HATEOAS V2 para mantenimientos")
public class MantenimientoControllerV2 {

    private final MantenimientoService mantenimientoService;
    private final MantenimientoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Mantenimiento>> obtenerTodos() {
        List<EntityModel<Mantenimiento>> mantenimientos = mantenimientoService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(mantenimientos,
                linkTo(methodOn(MantenimientoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Mantenimiento>> obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Mantenimiento>> crear(@RequestBody Mantenimiento mantenimiento) {
        Mantenimiento nuevo = mantenimientoService.guardarEntidad(mantenimiento);
        return ResponseEntity
                .created(linkTo(methodOn(MantenimientoControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Mantenimiento>> actualizar(@PathVariable Long id, @RequestBody Mantenimiento mantenimiento) {
        return mantenimientoService.obtenerEntidadPorId(id)
                .map(existing -> {
                    mantenimiento.setId(id);
                    Mantenimiento actualizado = mantenimientoService.guardarEntidad(mantenimiento);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (mantenimientoService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mantenimientoService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

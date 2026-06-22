package com.hotel.empleados.controller;

import com.hotel.empleados.assemblers.EmpleadoModelAssembler;
import com.hotel.empleados.model.Empleado;
import com.hotel.empleados.service.EmpleadoService;
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
@RequestMapping("/api/v2/empleados")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "API HATEOAS V2 para empleados")
public class EmpleadoControllerV2 {

    private final EmpleadoService empleadoService;
    private final EmpleadoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Empleado>> obtenerTodos() {
        List<EntityModel<Empleado>> empleados = empleadoService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(empleados,
                linkTo(methodOn(EmpleadoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Empleado>> obtenerPorId(@PathVariable Long id) {
        return empleadoService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Empleado>> crear(@RequestBody Empleado empleado) {
        Empleado nuevo = empleadoService.guardarEntidad(empleado);
        return ResponseEntity
                .created(linkTo(methodOn(EmpleadoControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Empleado>> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.obtenerEntidadPorId(id)
                .map(existing -> {
                    empleado.setId(id);
                    Empleado actualizado = empleadoService.guardarEntidad(empleado);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (empleadoService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        empleadoService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

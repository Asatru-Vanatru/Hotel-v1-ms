package com.hotel.mantenimiento.controller;

import java.util.stream.Collectors;
import java.util.List;

//import org.hibernate.mapping.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.mantenimiento.assembler.MantenimientoModelAssembler;
import com.hotel.mantenimiento.dto.MantenimientoRequestDTO;
import com.hotel.mantenimiento.dto.MantenimientoResponseDTO;
import com.hotel.mantenimiento.service.MantenimientoService;

import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/mantenimientos")
public class MantenimientoControllerV2 {

    private final MantenimientoService mantenimientoService;

    private final MantenimientoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para obtener todos los mantenimientos en formato HAL+JSON
    public CollectionModel<EntityModel<MantenimientoResponseDTO>> obtenerTodos() {

        List<EntityModel<MantenimientoResponseDTO>> mantenimientos = mantenimientoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(mantenimientos,
                linkTo(methodOn(MantenimientoControllerV2.class)
                .obtenerTodos())
                .withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para obtener un mantenimiento por su ID en formato HAL+JSON
    public ResponseEntity<EntityModel<MantenimientoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para crear un nuevo mantenimiento en formato HAL+JSON
    public ResponseEntity<EntityModel<MantenimientoResponseDTO>> crearMantenimiento(@RequestBody MantenimientoRequestDTO mantenimientoRequest) {
        MantenimientoResponseDTO nuevoMantenimiento = mantenimientoService.guardar(mantenimientoRequest);
        return ResponseEntity
                .created(linkTo(methodOn(MantenimientoControllerV2.class).obtenerPorId(nuevoMantenimiento.getId())).toUri())
                .body(assembler.toModel(nuevoMantenimiento));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para actualizar un mantenimiento existente en formato HAL+JSON
    public ResponseEntity<EntityModel<MantenimientoResponseDTO>> actualizarMantenimiento(@PathVariable Long id, @RequestBody MantenimientoRequestDTO mantenimientoRequest) {
        return mantenimientoService.actualizar(id, mantenimientoRequest)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para eliminar un mantenimiento por su ID en formato HAL+JSON
    public ResponseEntity<?> eliminarMantenimiento(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

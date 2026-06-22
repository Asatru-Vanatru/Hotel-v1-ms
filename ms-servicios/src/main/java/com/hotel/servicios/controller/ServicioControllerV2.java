package com.hotel.servicios.controller;

import com.hotel.servicios.assemblers.ServicioModelAssembler;
import com.hotel.servicios.model.Servicio;
import com.hotel.servicios.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/servicios")
public class ServicioControllerV2 {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ServicioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Servicio>> getAllServicios() {
        List<EntityModel<Servicio>> servicios = servicioService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(servicios,
                linkTo(methodOn(ServicioControllerV2.class).getAllServicios()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Servicio> getServicioById(@PathVariable Long id) {
        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        return assembler.toModel(servicio);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> createServicio(@RequestBody Servicio servicio) {
        Servicio newServicio = servicioService.save(servicio);
        return ResponseEntity
                .created(linkTo(methodOn(ServicioControllerV2.class).getServicioById(newServicio.getId())).toUri())
                .body(assembler.toModel(newServicio));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> updateServicio(@PathVariable Long id, @RequestBody Servicio servicio) {
        servicio.setId(id);
        Servicio updatedServicio = servicioService.save(servicio);
        return ResponseEntity.ok(assembler.toModel(updatedServicio));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> deleteServicio(@PathVariable Long id) {
        servicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

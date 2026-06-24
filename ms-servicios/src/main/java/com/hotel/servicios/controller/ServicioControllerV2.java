package com.hotel.servicios.controller;

import com.hotel.servicios.assemblers.ServicioModelAssembler;
import com.hotel.servicios.dto.ServicioRequestDTO;
import com.hotel.servicios.dto.ServicioResponseDTO;
import com.hotel.servicios.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public CollectionModel<EntityModel<ServicioResponseDTO>> getAllServicios() {
        List<EntityModel<ServicioResponseDTO>> servicios = servicioService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(servicios,
                linkTo(methodOn(ServicioControllerV2.class).getAllServicios()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ServicioResponseDTO> getServicioById(@PathVariable Long id) {
        ServicioResponseDTO servicio = servicioService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        return assembler.toModel(servicio);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ServicioResponseDTO>> createServicio(@Valid @RequestBody ServicioRequestDTO dto) {
        ServicioResponseDTO newServicio = servicioService.guardar(dto);
        return ResponseEntity
                .created(linkTo(methodOn(ServicioControllerV2.class).getServicioById(newServicio.getId())).toUri())
                .body(assembler.toModel(newServicio));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ServicioResponseDTO>> updateServicio(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO dto) {
        ServicioResponseDTO updatedServicio = servicioService.actualizar(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        return ResponseEntity.ok(assembler.toModel(updatedServicio));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

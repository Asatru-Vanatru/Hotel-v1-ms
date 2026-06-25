package com.hotel.habitaciones.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.hotel.habitaciones.assembler.HabitacionModelAssembler;
import com.hotel.habitaciones.dto.HabitacionRequestDTO;
import com.hotel.habitaciones.dto.HabitacionResponseDTO;
import com.hotel.habitaciones.service.HabitacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/habitaciones")
public class HabitacionControllerV2 {

    private final HabitacionService habitacionService;// Servicio de negocio para habitaciones

    private final HabitacionModelAssembler assembler;// Ensamblador para convertir DTOs a modelos HATEOAS

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para obtener todas las habitaciones en formato HAL+JSON
    public CollectionModel<EntityModel<HabitacionResponseDTO>> obtenerTodos() {

        List<EntityModel<HabitacionResponseDTO>> habitaciones = habitacionService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(habitaciones,
            linkTo(methodOn(HabitacionControllerV2.class)
            .obtenerTodos())
            .withSelfRel()
        );
                
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para obtener una habitación por su ID en formato HAL+JSON
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return habitacionService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());            
        
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para crear una nueva habitación en formato HAL+JSON
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> crearHabitacion(@Valid @RequestBody HabitacionRequestDTO habitacionRequest) {

        HabitacionResponseDTO nuevaHabitacion = habitacionService.guardar(habitacionRequest);
        return ResponseEntity
                .created(linkTo(methodOn(HabitacionControllerV2.class).obtenerPorId(nuevaHabitacion.getId())).toUri())
                .body(assembler.toModel(nuevaHabitacion));  
                
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para actualizar una habitación existente en formato HAL+JSON
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> actualizarHabitacion(@PathVariable Long id, @Valid @RequestBody HabitacionRequestDTO habitacionRequest) {
        return habitacionService.actualizar(id, habitacionRequest)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para eliminar una habitación por su ID en formato HAL+JSON
    public ResponseEntity<?> eliminarHabitacion(@PathVariable Long id) {
        if (habitacionService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        habitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

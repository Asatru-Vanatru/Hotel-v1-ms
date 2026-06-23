package com.hotel.habitaciones.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.habitaciones.assembler.HabitacionModelAssembler;
import com.hotel.habitaciones.dto.HabitacionResponseDTO;
import com.hotel.habitaciones.service.HabitacionService;

import lombok.RequiredArgsConstructor;

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
                
            
        
    }

}

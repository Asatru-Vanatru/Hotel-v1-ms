package com.hotel.habitaciones.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.habitaciones.controller.HabitacionController;
import com.hotel.habitaciones.dto.HabitacionResponseDTO;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HabitacionModelAssembler implements RepresentationModelAssembler<HabitacionResponseDTO, EntityModel<HabitacionResponseDTO>> {

    @Override
    public EntityModel<HabitacionResponseDTO> toModel(HabitacionResponseDTO habitacion) {
        return EntityModel.of(habitacion,
                linkTo(methodOn(HabitacionController.class).obtenerPorId(habitacion.getId())).withSelfRel(),
                linkTo(methodOn(HabitacionController.class).obtenerTodos()).withRel("habitaciones"));
    }

}

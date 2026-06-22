package com.hotel.habitaciones.assemblers;

import com.hotel.habitaciones.controller.HabitacionControllerV2;
import com.hotel.habitaciones.model.Habitacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HabitacionModelAssembler implements RepresentationModelAssembler<Habitacion, EntityModel<Habitacion>> {

    @Override
    public EntityModel<Habitacion> toModel(Habitacion habitacion) {
        return EntityModel.of(habitacion,
                linkTo(methodOn(HabitacionControllerV2.class).obtenerPorId(habitacion.getId())).withSelfRel(),
                linkTo(methodOn(HabitacionControllerV2.class).obtenerTodas()).withRel("habitaciones"));
    }
}

package com.hotel.mantenimiento.assemblers;

import com.hotel.mantenimiento.controller.MantenimientoControllerV2;
import com.hotel.mantenimiento.model.Mantenimiento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MantenimientoModelAssembler implements RepresentationModelAssembler<Mantenimiento, EntityModel<Mantenimiento>> {

    @Override
    public EntityModel<Mantenimiento> toModel(Mantenimiento mantenimiento) {
        return EntityModel.of(mantenimiento,
                linkTo(methodOn(MantenimientoControllerV2.class).obtenerPorId(mantenimiento.getId())).withSelfRel(),
                linkTo(methodOn(MantenimientoControllerV2.class).obtenerTodos()).withRel("mantenimientos"));
    }
}

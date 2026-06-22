package com.hotel.empleados.assemblers;

import com.hotel.empleados.controller.EmpleadoControllerV2;
import com.hotel.empleados.model.Empleado;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpleadoModelAssembler implements RepresentationModelAssembler<Empleado, EntityModel<Empleado>> {

    @Override
    public EntityModel<Empleado> toModel(Empleado empleado) {
        return EntityModel.of(empleado,
                linkTo(methodOn(EmpleadoControllerV2.class).obtenerPorId(empleado.getId())).withSelfRel(),
                linkTo(methodOn(EmpleadoControllerV2.class).obtenerTodos()).withRel("empleados"));
    }
}

package com.hotel.reservas.assemblers;

import com.hotel.reservas.controller.ReservaControllerV2;
import com.hotel.reservas.model.Reserva;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<Reserva, EntityModel<Reserva>> {

    @Override
    public EntityModel<Reserva> toModel(Reserva reserva) {
        return EntityModel.of(reserva,
                linkTo(methodOn(ReservaControllerV2.class).obtenerPorId(reserva.getId())).withSelfRel(),
                linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withRel("reservas"));
    }
}

package com.hotel.pagos.assemblers;

import com.hotel.pagos.controller.PagoControllerV2;
import com.hotel.pagos.model.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {

    @Override
    public EntityModel<Pago> toModel(Pago pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagoControllerV2.class).obtenerPorId(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("pagos"));
    }
}

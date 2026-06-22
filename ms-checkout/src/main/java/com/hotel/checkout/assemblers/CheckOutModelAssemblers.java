package com.hotel.checkout.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hotel.checkout.controller.CheckOutControllerV2;
import com.hotel.checkout.model.CheckOut;

@Component
public class CheckOutModelAssemblers implements RepresentationModelAssembler<CheckOut, EntityModel<CheckOut>> {

    @Override
    public EntityModel<CheckOut> toModel(CheckOut checkOut) {
        return EntityModel.of(checkOut,
            linkTo(methodOn(CheckOutControllerV2.class).obtenerPorId(checkOut.getId())).withSelfRel(),
            linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withRel("checkouts")
        );
    }

}

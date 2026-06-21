package com.hotel.checkout.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hotel.checkout.controller.CheckOutController;
import com.hotel.checkout.model.CheckOut;

@Component
public class CheckOutModelAssemblers implements RepresentationModelAssembler<CheckOut, EntityModel<CheckOut>> {

    @Override
    public EntityModel<CheckOut> toModel(CheckOut checkOut) {
        return EntityModel.of(checkOut,
            linkTo(methodOn(CheckOutControllerV2.class).obtenerPorId(checkOut.getId())).withSelfRel(),
            linkTo(methodOn(CheckOutController.class).obtenerTodos()).withRel("checkouts")
        );
    }

}

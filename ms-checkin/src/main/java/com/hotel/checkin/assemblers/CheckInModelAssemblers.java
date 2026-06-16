package com.hotel.checkin.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hotel.checkin.controller.CheckInController;
import com.hotel.checkin.model.CheckIn;

@Component
public class CheckInModelAssemblers implements RepresentationModelAssembler<CheckIn, EntityModel<CheckIn>> {

    @Override
    public EntityModel<CheckIn> toModel(CheckIn checkIn) {
        return EntityModel.of(checkIn,
            linkTo(methodOn(CheckInController.class).obtenerPorId(checkIn.getId())).withSelfRel(),
            linkTo(methodOn(CheckInController.class).obtenerTodos()).withRel("checkins")
        );
    }    
    

}

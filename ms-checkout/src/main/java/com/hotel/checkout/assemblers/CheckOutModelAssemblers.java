package com.hotel.checkout.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hotel.checkout.controller.CheckOutControllerV2;
import com.hotel.checkout.dto.CheckOutResponseDTO;


@Component
public class CheckOutModelAssemblers implements RepresentationModelAssembler<CheckOutResponseDTO, EntityModel<CheckOutResponseDTO>> {

    @Override
    public EntityModel<CheckOutResponseDTO> toModel(CheckOutResponseDTO checkOutResponseDTO) {
        return EntityModel.of(checkOutResponseDTO,
            linkTo(methodOn(CheckOutControllerV2.class).obtenerPorId(checkOutResponseDTO.getId())).withSelfRel(),
            linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withRel("checkouts")
        );
    }

}

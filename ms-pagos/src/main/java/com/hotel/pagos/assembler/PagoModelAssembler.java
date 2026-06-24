package com.hotel.pagos.assembler;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.pagos.controller.PagoController;
import com.hotel.pagos.dto.PagoResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoResponseDTO, EntityModel<PagoResponseDTO>> {

    @Override
    public EntityModel<PagoResponseDTO> toModel(PagoResponseDTO pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagoController.class).obtenerPorId(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoController.class).obtenerTodos()).withRel("pagos"));
    }

}

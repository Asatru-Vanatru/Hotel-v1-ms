package com.hotel.reservas.assemblers;

import com.hotel.reservas.controller.ReservaController;
import com.hotel.reservas.dto.ReservaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaResponseDTO, EntityModel<ReservaResponseDTO>> {

    @Override
    public EntityModel<ReservaResponseDTO> toModel(ReservaResponseDTO reserva) {
        return EntityModel.of(reserva,
            linkTo(methodOn(ReservaController.class).obtenerPorId(reserva.getId())).withSelfRel(),
            linkTo(methodOn(ReservaController.class).obtenerTodas()).withRel("reservas")
        );
    }

}

package com.hotel.consumos.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.hotel.consumos.controller.ConsumoControllerV2;
import com.hotel.consumos.dto.ConsumoResponseDTO;


@Component
public class ConsumosModelAssemblers implements RepresentationModelAssembler<ConsumoResponseDTO, EntityModel<ConsumoResponseDTO>> {

    @Override
    public EntityModel<ConsumoResponseDTO> toModel(ConsumoResponseDTO consumo) {
        return EntityModel.of(consumo,
            linkTo(methodOn(ConsumoControllerV2.class).obtenerPorId(consumo.getId())).withSelfRel(),
            linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withRel("consumos")
        );
    }

}

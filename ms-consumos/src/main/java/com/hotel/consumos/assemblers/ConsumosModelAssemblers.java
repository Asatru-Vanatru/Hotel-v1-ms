package com.hotel.consumos.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.hotel.consumos.controller.ConsumoControllerV2;
import com.hotel.consumos.model.Consumo;


@Component
public class ConsumosModelAssemblers implements RepresentationModelAssembler<Consumo, EntityModel<Consumo>> {

    @Override
    public EntityModel<Consumo> toModel(Consumo consumo) {
        return EntityModel.of(consumo,
            linkTo(methodOn(ConsumoControllerV2.class).obtenerPorId(consumo.getId())).withSelfRel(),
            linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withRel("consumos")
        );
    }

}

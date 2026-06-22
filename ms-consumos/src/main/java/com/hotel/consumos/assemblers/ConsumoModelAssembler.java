package main.java.com.hotel.consumos.assemblers;

import com.hotel.consumos.controller.ConsumoControllerV2;
import com.hotel.consumos.model.Consumo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsumoModelAssembler implements RepresentationModelAssembler<Consumo, EntityModel<Consumo>> {

    @Override
    public EntityModel<Consumo> toModel(Consumo consumo) {
        return EntityModel.of(consumo,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerPorId(consumo.getId())).withSelfRel(),
                linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withRel("consumos"));
    }
}

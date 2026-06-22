package com.hotel.clientes.assemblers;

import com.hotel.clientes.controller.ClienteControllerV2;
import com.hotel.clientes.model.Cliente;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes"));
    }
}

package com.hotel.clientes.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.hotel.clientes.controller.ClienteControllerV2;
import com.hotel.clientes.dto.ClienteResponseDTO;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {

    @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes"));


    }
}    

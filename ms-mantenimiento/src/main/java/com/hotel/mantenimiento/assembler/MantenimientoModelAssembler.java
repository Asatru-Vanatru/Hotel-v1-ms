package com.hotel.mantenimiento.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hotel.mantenimiento.controller.MantenimientoController;
import com.hotel.mantenimiento.dto.MantenimientoResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MantenimientoModelAssembler implements RepresentationModelAssembler<MantenimientoResponseDTO, EntityModel<MantenimientoResponseDTO>> {

    @Override
    public EntityModel<MantenimientoResponseDTO> toModel(MantenimientoResponseDTO mantenimiento) {
        return EntityModel.of(mantenimiento,
                linkTo(methodOn(MantenimientoController.class).obtenerPorId(mantenimiento.getId())).withSelfRel(),
                linkTo(methodOn(MantenimientoController.class).obtenerTodos()).withRel("mantenimientos"));
    }

}

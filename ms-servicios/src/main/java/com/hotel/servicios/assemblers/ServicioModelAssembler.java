package com.hotel.servicios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hotel.servicios.controller.ServicioControllerV2;
import com.hotel.servicios.dto.ServicioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ServicioModelAssembler implements RepresentationModelAssembler<ServicioResponseDTO, EntityModel<ServicioResponseDTO>> {

    @Override
    public EntityModel<ServicioResponseDTO> toModel(ServicioResponseDTO servicio) {
        return EntityModel.of(servicio,
                linkTo(methodOn(ServicioControllerV2.class).getServicioById(servicio.getId())).withSelfRel(),
                linkTo(methodOn(ServicioControllerV2.class).getAllServicios()).withRel("servicios"));
    }
}

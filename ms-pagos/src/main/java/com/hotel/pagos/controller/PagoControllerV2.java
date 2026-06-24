package com.hotel.pagos.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.pagos.assembler.PagoModelAssembler;
import com.hotel.pagos.dto.PagoRequestDTO;
import com.hotel.pagos.dto.PagoResponseDTO;
import com.hotel.pagos.service.PagoService;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/pagos")
public class PagoControllerV2 {


    private final PagoService pagoService;


    private final PagoModelAssembler assembler;


    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PagoResponseDTO>> obtenerTodos() {

        List<EntityModel<PagoResponseDTO>> pagos= pagoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
      return CollectionModel.of(pagos,
            linkTo(methodOn(PagoControllerV2.class)
            .obtenerTodos())
            .withSelfRel()
        );    
    }

    @GetMapping(value ="/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoResponseDTO>> crearPago(@RequestBody PagoRequestDTO pagoRequest) {
        PagoResponseDTO nuevoPago = pagoService.registrar(pagoRequest);
        URI location = linkTo(methodOn(PagoControllerV2.class).obtenerPorId(nuevoPago.getId())).toUri();
        EntityModel<PagoResponseDTO> model = assembler.toModel(nuevoPago);
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoResponseDTO>> actualizarPago(@PathVariable Long id, @RequestBody PagoRequestDTO pagoRequest) {
        return pagoService.actualizar(id, pagoRequest)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());    
    }

    
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)// Endpoint para eliminar una habitación por su ID en formato HAL+JSON
    public ResponseEntity<?> eliminarPago(@PathVariable Long id){
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }




}

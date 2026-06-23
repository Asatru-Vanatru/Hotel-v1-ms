package com.hotel.clientes.controller;

import com.hotel.clientes.assemblers.ClienteModelAssembler;
import com.hotel.clientes.service.ClienteService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import com.hotel.clientes.dto.ClienteResponseDTO;
import com.hotel.clientes.dto.ClienteRequestDTO;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;




@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteControllerV2 {

    
    private final ClienteService clienteService;

    
    private final ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ClienteResponseDTO>> obtenerTodos() {

        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class)
                .obtenerTodos())
                .withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDTO>> crearCliente (@RequestBody ClienteRequestDTO clienteRequest) {
        ClienteResponseDTO nuevoCliente = clienteService.guardar(clienteRequest);
        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(nuevoCliente.getId())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteResponseDTO>> actualizarCliente(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequest) {
        return clienteService.actualizar(id, clienteRequest)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}


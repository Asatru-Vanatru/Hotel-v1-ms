package com.hotel.clientes.controller;

import com.hotel.clientes.assemblers.ClienteModelAssembler;
import com.hotel.clientes.model.Cliente;
import com.hotel.clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API HATEOAS V2 para clientes")
public class ClienteControllerV2 {

    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Cliente>> obtenerTodos() {
        List<EntityModel<Cliente>> clientes = clienteService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> crear(@RequestBody Cliente cliente) {
        Cliente nuevo = clienteService.guardarEntidad(cliente);
        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Cliente>> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.obtenerEntidadPorId(id)
                .map(existing -> {
                    cliente.setId(id);
                    Cliente actualizado = clienteService.guardarEntidad(cliente);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (clienteService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

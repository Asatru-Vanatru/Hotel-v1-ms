package com.hotel.clientes.controller;

import com.hotel.clientes.service.ClienteService;

import javax.annotation.processing.SupportedAnnotationTypes;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteControllerV2 {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    public ClienteModelAssemblers assembler;

    @GetMapping(produces = MediaTypes.Hal_JSON_VALUE)
    public CollectionModel<EntityModel<Cliente>> obtenerTodos() {
        List<EntityModel<Cliente>> clientes = clienteService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Cliente> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return assembler.toModel(cliente);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntityModel<EntityModel<Cliente>> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.save(cliente);
        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(nuevoCliente.getId())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntityModel<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.save(cliente);
        return ResponseEntity
                .ok(assembler.toModel(clienteActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}


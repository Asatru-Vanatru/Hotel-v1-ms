package com.hotel.consumos.controller;

import com.hotel.consumos.assemblers.ConsumoModelAssembler;
import com.hotel.consumos.model.Consumo;
import com.hotel.consumos.service.ConsumoService;
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
@RequestMapping("/api/v2/consumos")
@RequiredArgsConstructor
@Tag(name = "Consumos", description = "API HATEOAS V2 para consumos")
public class ConsumoControllerV2 {

    private final ConsumoService consumoService;
    private final ConsumoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Consumo>> obtenerTodos() {
        List<EntityModel<Consumo>> consumos = consumoService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(consumos,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> obtenerPorId(@PathVariable Long id) {
        return consumoService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> registrar(@RequestBody Consumo consumo) {
        Consumo nuevo = consumoService.guardarEntidad(consumo);
        return ResponseEntity
                .created(linkTo(methodOn(ConsumoControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> actualizar(@PathVariable Long id, @RequestBody Consumo consumo) {
        return consumoService.obtenerEntidadPorId(id)
                .map(existing -> {
                    consumo.setId(id);
                    Consumo actualizado = consumoService.guardarEntidad(consumo);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (consumoService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        consumoService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

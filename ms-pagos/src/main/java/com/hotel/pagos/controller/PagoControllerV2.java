package com.hotel.pagos.controller;

import com.hotel.pagos.assemblers.PagoModelAssembler;
import com.hotel.pagos.model.Pago;
import com.hotel.pagos.service.PagoService;
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
@RequestMapping("/api/v2/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "API HATEOAS V2 para pagos")
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Pago>> obtenerTodos() {
        List<EntityModel<Pago>> pagos = pagoService.obtenerTodasEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pagos,
                linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Pago>> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerEntidadPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Pago>> registrar(@RequestBody Pago pago) {
        Pago nuevo = pagoService.guardarEntidad(pago);
        return ResponseEntity
                .created(linkTo(methodOn(PagoControllerV2.class).obtenerPorId(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Pago>> actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        return pagoService.obtenerEntidadPorId(id)
                .map(existing -> {
                    pago.setId(id);
                    Pago actualizado = pagoService.guardarEntidad(pago);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pagoService.obtenerEntidadPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }
}

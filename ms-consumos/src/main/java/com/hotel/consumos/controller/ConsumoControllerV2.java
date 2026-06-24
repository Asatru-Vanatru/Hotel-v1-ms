package com.hotel.consumos.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.hotel.consumos.assemblers.ConsumosModelAssemblers;
import com.hotel.consumos.dto.ConsumoRequestDTO;
import com.hotel.consumos.dto.ConsumoResponseDTO;
import com.hotel.consumos.service.ConsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.IanaLinkRelations;


@RestController
@RequestMapping("/api/v2/consumos")
public class ConsumoControllerV2 {

    @Autowired
    private ConsumoService consumosService;

    @Autowired
    private ConsumosModelAssemblers assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ConsumoResponseDTO>> obtenerTodos() {
        List<EntityModel<ConsumoResponseDTO>> consumos = consumosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(consumos,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ConsumoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return consumosService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ConsumoResponseDTO>> registrar(@RequestBody ConsumoRequestDTO consumo) {
        ConsumoResponseDTO nuevoConsumo = consumosService.registrar(consumo);
        EntityModel<ConsumoResponseDTO> entityModel = assembler.toModel(nuevoConsumo);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ConsumoResponseDTO>> actualizar(@PathVariable Long id, @RequestBody ConsumoRequestDTO consumo) {
        return consumosService.actualizar(id, consumo)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (consumosService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        consumosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<CollectionModel<EntityModel<ConsumoResponseDTO>>> obtenerPorReserva(@PathVariable Long reservaId) {
        List<EntityModel<ConsumoResponseDTO>> consumos = consumosService.buscarPorReserva(reservaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ConsumoResponseDTO>> collectionModel = CollectionModel.of(consumos,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerPorReserva(reservaId)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/reserva/{reservaId}/total")
    public ResponseEntity<EntityModel<BigDecimal>> calcularTotalReserva(@PathVariable Long reservaId) {
        BigDecimal total = consumosService.calcularTotalPorReserva(reservaId);
        EntityModel<BigDecimal> model = EntityModel.of(total,
                linkTo(methodOn(ConsumoControllerV2.class).calcularTotalReserva(reservaId)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/estadisticas/mas-consumidos")
    public ResponseEntity<List<Object[]>> obtenerMasConsumidos() {
        return ResponseEntity.ok(consumosService.obtenerServiciosMasConsumidos());
    }

}


package com.hotel.consumos.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.hotel.consumos.assemblers.ConsumosModelAssemblers;
import com.hotel.consumos.model.Consumo;
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
    public CollectionModel<EntityModel<Consumo>> obtenerTodos() {
        List<EntityModel<Consumo>> consumos = consumosService.obtenerTodosEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(consumos,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> obtenerPorId(@PathVariable Long id) {
        Consumo consumo = consumosService.obtenerEntidadPorId(id);
        if (consumo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(assembler.toModel(consumo));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> registrar(@RequestBody Consumo consumo){
        Consumo nuevoConsumo = consumosService.registrarEntidad(consumo);
        EntityModel<Consumo> entityModel = assembler.toModel(nuevoConsumo);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Consumo>> actualizar(@PathVariable Long id, @RequestBody Consumo consumo){
        Consumo consumoActualizado = consumosService.actualizarEntidad(id, consumo);
        EntityModel<Consumo> entityModel = assembler.toModel(consumoActualizado);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (consumosService.obtenerEntidadPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        consumosService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<CollectionModel<EntityModel<Consumo>>> obtenerPorReserva(@PathVariable Long reservaId) {
        List<EntityModel<Consumo>> consumos = consumosService.obtenerEntidadesPorReserva(reservaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                CollectionModel<EntityModel<Consumo>> collectionModel = CollectionModel.of(consumos,
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
    public ResponseEntity<CollectionModel<EntityModel<Consumo>>> obtenerMasConsumidos() {
        List<EntityModel<Consumo>> consumos = consumosService.obtenerMasConsumidos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                CollectionModel<EntityModel<Consumo>> collectionModel = CollectionModel.of(consumos,
                linkTo(methodOn(ConsumoControllerV2.class).obtenerMasConsumidos()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

}

package com.hotel.checkout.controller;

import com.hotel.checkout.assemblers.CheckOutModelAssemblers;
import com.hotel.checkout.model.CheckOut;
import com.hotel.checkout.service.CheckOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/v2/checkouts")
public class CheckOutControllerV2 {

    @Autowired
    private CheckOutService checkOutService;

    @Autowired
    private CheckOutModelAssemblers assembler;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckOut>> obtenerTodos() {
        List<EntityModel<CheckOut>> checkOuts = checkOutService.obtenerTodosEntidades().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkOuts,
                linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOut>> obtenerPorId(@PathVariable Long id) {
    CheckOut checkOut = checkOutService.obtenerEntidadPorId(id);
    if (checkOut == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(assembler.toModel(checkOut));
}

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOut>> registrar(@RequestBody CheckOut checkOut){
        CheckOut nuevoCheckOut = checkOutService.registrarEntidad(checkOut);
        EntityModel<CheckOut> entityModel = assembler.toModel(nuevoCheckOut);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOut>> actualizar(@PathVariable Long id, @RequestBody CheckOut checkOut){
        CheckOut checkOutActualizado = checkOutService.actualizarEntidad(id, checkOut);
        EntityModel<CheckOut> entityModel = assembler.toModel(checkOutActualizado);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        checkOutService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/estadisticas/ingresos", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BigDecimal>> obtenerIngresosTotales() {
        BigDecimal ingresos = checkOutService.obtenerIngresosTotales();
        EntityModel<BigDecimal> model = EntityModel.of(ingresos,
            linkTo(methodOn(CheckOutControllerV2.class).obtenerIngresosTotales()).withSelfRel(),
            linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withRel("checkouts")
        );
        return ResponseEntity.ok(model);
    }

    

    @GetMapping(value = "/reserva/{reservaId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOut>> obtenerPorReservaId(@PathVariable Long reservaId) {
        CheckOut checkOut = checkOutService.obtenerEntidadPorReservaId(reservaId);
        if (checkOut == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(checkOut));
    }

    @GetMapping(value = "/estadisticas/promedio-estancia", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BigDecimal>> obtenerPromedioEstancia() {
        BigDecimal promedioEstancia = checkOutService.obtenerPromedioEstancia();
        EntityModel<BigDecimal> model = EntityModel.of(promedioEstancia,
            linkTo(methodOn(CheckOutControllerV2.class).obtenerPromedioEstancia()).withSelfRel(),
            linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withRel("checkouts")
        );
        return ResponseEntity.ok(model);
}
}

package com.hotel.checkout.controller;

import com.hotel.checkout.assemblers.CheckOutModelAssemblers;
import com.hotel.checkout.dto.CheckOutRequestDTO;
import com.hotel.checkout.dto.CheckOutResponseDTO;
import com.hotel.checkout.service.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/checkouts")
public class CheckOutControllerV2 {

    
    private final CheckOutService checkOutService;

    private final CheckOutModelAssemblers assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CheckOutResponseDTO>> obtenerTodos() {
        List<EntityModel<CheckOutResponseDTO>> checkOuts = checkOutService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(checkOuts,
                linkTo(methodOn(CheckOutControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOutResponseDTO>> obtenerPorId(@PathVariable Long id) {
    return checkOutService.obtenerPorId(id)
            .map(assembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOutResponseDTO>> registrar(@RequestBody CheckOutRequestDTO checkOut){
        CheckOutResponseDTO nuevoCheckOut = checkOutService.registrar(checkOut);
        return ResponseEntity.created(linkTo(methodOn(CheckOutControllerV2.class).obtenerPorId(nuevoCheckOut.getId())).toUri())
                .body(assembler.toModel(nuevoCheckOut));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CheckOutResponseDTO>> actualizar(@PathVariable Long id, @RequestBody CheckOutRequestDTO checkOut){
        return checkOutService.actualizar(id, checkOut)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<EntityModel<CheckOutResponseDTO>> obtenerPorReservaId(@PathVariable Long reservaId) {
        return checkOutService.buscarPorReserva(reservaId)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

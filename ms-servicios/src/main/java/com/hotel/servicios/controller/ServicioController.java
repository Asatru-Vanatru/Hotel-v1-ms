// Paquete de la capa web del microservicio ms-servicios
package com.hotel.servicios.controller;

import com.hotel.servicios.dto.ServicioRequestDTO; // DTO de entrada validado para el body
import com.hotel.servicios.dto.ServicioResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.servicios.service.ServicioService; // Servicio con la lógica del catálogo
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de la respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.math.BigDecimal; // Tipo para el parámetro de precio máximo en la búsqueda
import java.util.List; // Lista para endpoints que devuelven múltiples servicios

// Controller REST del microservicio ms-servicios
// URL base: http://localhost:8086/api/servicios
@RestController // Todos los métodos retornan JSON automáticamente
@RequestMapping("/api/servicios") // Ruta base del catálogo de servicios adicionales
@RequiredArgsConstructor // Inyecta el servicio por constructor
public class ServicioController {

    private final ServicioService servicioService; // Servicio de lógica del catálogo de servicios

    // GET /api/servicios → 200 con el catálogo completo
    @GetMapping // Mapea GET a la URL base del catálogo
    public ResponseEntity<List<ServicioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(servicioService.obtenerTodos()); // 200 con catálogo completo
    }

    // GET /api/servicios/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/servicios/{id}
    public ResponseEntity<ServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return servicioService.obtenerPorId(id) // Busca el servicio por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/servicios → 201 Created con el servicio creado
    @PostMapping // Mapea POST para agregar un servicio al catálogo
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO dto) {
        return ResponseEntity.status(201).body(servicioService.guardar(dto)); // 201 con nuevo servicio
    }

    // PUT /api/servicios/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/servicios/{id}
    public ResponseEntity<ServicioResponseDTO> actualizar(
            @PathVariable Long id, // ID del servicio a actualizar
            @Valid @RequestBody ServicioRequestDTO dto) { // Body validado
        return servicioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/servicios/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/servicios/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (servicioService.obtenerPorId(id).isEmpty()) { // Verifica si el servicio existe
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        servicioService.eliminar(id); // Elimina el servicio del catálogo
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/servicios/categoria/SPA → filtra por categoría del servicio
    @GetMapping("/categoria/{categoria}") // Mapea GET /api/servicios/categoria/{nombre}
    public ResponseEntity<List<ServicioResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(servicioService.buscarPorCategoria(categoria)); // 200 filtrado
    }

    // GET /api/servicios/disponibles → solo los servicios activos del catálogo
    @GetMapping("/disponibles") // Mapea GET /api/servicios/disponibles
    public ResponseEntity<List<ServicioResponseDTO>> buscarDisponibles() {
        return ResponseEntity.ok(servicioService.buscarPorDisponibilidad(true)); // 200 con disponibles
    }

    // GET /api/servicios/precio?max=50.00 → servicios hasta un precio máximo (JPQL)
    @GetMapping("/precio") // Mapea GET /api/servicios/precio con parámetro max
    public ResponseEntity<List<ServicioResponseDTO>> buscarHastaPrecio(@RequestParam BigDecimal max) {
        return ResponseEntity.ok(servicioService.buscarHastaPrecio(max)); // 200 con JPQL
    }

    // GET /api/servicios/catalogo → catálogo disponible ordenado (SQL nativo)
    @GetMapping("/catalogo") // Mapea GET /api/servicios/catalogo para la vista de recepción
    public ResponseEntity<List<ServicioResponseDTO>> obtenerCatalogo() {
        return ResponseEntity.ok(servicioService.obtenerCatalogoOrdenado()); // 200 con SQL nativo
    }

}

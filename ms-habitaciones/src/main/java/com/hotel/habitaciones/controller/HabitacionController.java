// Paquete de la capa web del microservicio ms-habitaciones
package com.hotel.habitaciones.controller;

import com.hotel.habitaciones.dto.HabitacionRequestDTO; // DTO de entrada para el body
import com.hotel.habitaciones.dto.HabitacionResponseDTO; // DTO de salida para la respuesta
import com.hotel.habitaciones.service.HabitacionService; // Servicio con la lógica de negocio
import jakarta.validation.Valid; // Activa la validación del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Permite controlar el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Todas las anotaciones REST de Spring MVC
import java.math.BigDecimal; // Tipo para los parámetros de precio en búsqueda por rango
import java.util.List; // Lista para endpoints que devuelven múltiples habitaciones

// Controller REST del microservicio ms-habitaciones
// URL base: http://localhost:8082/api/habitaciones
@RestController // Indica que todos los métodos devuelven JSON (REST)
@RequestMapping("/api/habitaciones") // Ruta base para todos los endpoints de habitaciones
@RequiredArgsConstructor // Inyecta el servicio por constructor sin necesidad de @Autowired
public class HabitacionController {

    private final HabitacionService habitacionService; // Servicio de lógica de negocio

    // GET /api/habitaciones → 200 OK con todo el catálogo de habitaciones
    @GetMapping // Mapea peticiones GET sin ruta adicional
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(habitacionService.obtenerTodas()); // 200 con catálogo completo
    }

    // GET /api/habitaciones/{id} → 200 OK si existe, 404 si no
    @GetMapping("/{id}") // Mapea GET /api/habitaciones/{id}
    public ResponseEntity<HabitacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return habitacionService.obtenerPorId(id) // Busca por id en la base de datos
                .map(ResponseEntity::ok)                    // 200 con los datos de la habitación
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/habitaciones → 201 Created con la habitación creada
    @PostMapping // Mapea peticiones POST a la URL base
    public ResponseEntity<HabitacionResponseDTO> crear(@Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.status(201).body(habitacionService.guardar(dto)); // 201 con recurso creado
    }

    // PUT /api/habitaciones/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/habitaciones/{id}
    public ResponseEntity<HabitacionResponseDTO> actualizar(
            @PathVariable Long id, // ID de la habitación a actualizar
            @Valid @RequestBody HabitacionRequestDTO dto) { // Body validado
        return habitacionService.actualizar(id, dto)
                .map(ResponseEntity::ok)                    // 200 con datos actualizados
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/habitaciones/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/habitaciones/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (habitacionService.obtenerPorId(id).isEmpty()) { // Verifica si existe
            return ResponseEntity.notFound().build(); // 404 si no existe la habitación
        }
        habitacionService.eliminar(id); // Elimina de la base de datos
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }

    // GET /api/habitaciones/estado/DISPONIBLE → filtra por estado
    @GetMapping("/estado/{estado}") // Mapea GET con path variable del estado
    public ResponseEntity<List<HabitacionResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(habitacionService.buscarPorEstado(estado)); // 200 con lista filtrada
    }

    // GET /api/habitaciones/tipo/SUITE → filtra por tipo de habitación
    @GetMapping("/tipo/{tipo}") // Mapea GET con path variable del tipo
    public ResponseEntity<List<HabitacionResponseDTO>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(habitacionService.buscarPorTipo(tipo)); // 200 con lista filtrada
    }

    // GET /api/habitaciones/disponibles/tipo/DOBLE → disponibles de un tipo (JPQL)
    @GetMapping("/disponibles/tipo/{tipo}") // Mapea GET para disponibles de un tipo
    public ResponseEntity<List<HabitacionResponseDTO>> buscarDisponiblesPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(habitacionService.buscarDisponiblesPorTipo(tipo)); // 200 con JPQL
    }

    // GET /api/habitaciones/precio?min=50.00&max=200.00 → filtra por rango de precio (JPQL)
    @GetMapping("/precio") // Mapea GET /api/habitaciones/precio con parámetros de consulta
    public ResponseEntity<List<HabitacionResponseDTO>> buscarPorPrecio(
            @RequestParam BigDecimal min, // Precio mínimo del rango
            @RequestParam BigDecimal max) { // Precio máximo del rango
        return ResponseEntity.ok(habitacionService.buscarPorRangoPrecio(min, max)); // 200 con JPQL rango
    }

    // GET /api/habitaciones/disponibles → todas las disponibles ordenadas por precio (SQL nativo)
    @GetMapping("/disponibles") // Mapea GET /api/habitaciones/disponibles
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerDisponibles() {
        return ResponseEntity.ok(habitacionService.obtenerDisponiblesOrdenadas()); // 200 con SQL nativo
    }

}

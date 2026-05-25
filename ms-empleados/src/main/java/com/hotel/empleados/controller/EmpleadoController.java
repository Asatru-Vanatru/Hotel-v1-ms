// Paquete de la capa web del microservicio ms-empleados
package com.hotel.empleados.controller;

import com.hotel.empleados.dto.EmpleadoRequestDTO; // DTO de entrada validado para el body
import com.hotel.empleados.dto.EmpleadoResponseDTO; // DTO de salida para la respuesta JSON
import com.hotel.empleados.service.EmpleadoService; // Servicio con la lógica de empleados
import jakarta.validation.Valid; // Activa las validaciones del RequestDTO
import lombok.RequiredArgsConstructor; // Genera constructor de inyección del servicio
import org.springframework.http.ResponseEntity; // Controla el código HTTP de respuesta
import org.springframework.web.bind.annotation.*; // Anotaciones REST de Spring MVC
import java.util.List; // Lista para endpoints con múltiples empleados

// Controller REST del microservicio ms-empleados
// URL base: http://localhost:8089/api/empleados
@RestController // Todos los métodos retornan JSON sin configuración adicional
@RequestMapping("/api/empleados") // Ruta base de todos los endpoints de gestión de personal
@RequiredArgsConstructor // Inyecta el servicio por constructor sin @Autowired
public class EmpleadoController {

    private final EmpleadoService empleadoService; // Servicio de lógica de gestión de empleados

    // GET /api/empleados → 200 con lista de todos los empleados del hotel
    @GetMapping // Mapea GET a la URL base de empleados
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(empleadoService.obtenerTodos()); // 200 con lista completa
    }

    // GET /api/empleados/{id} → 200 OK o 404 Not Found
    @GetMapping("/{id}") // Mapea GET /api/empleados/{id}
    public ResponseEntity<EmpleadoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return empleadoService.obtenerPorId(id) // Busca el empleado por id
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // POST /api/empleados → 201 Created con el empleado creado
    @PostMapping // Mapea POST para registrar un nuevo empleado
    public ResponseEntity<EmpleadoResponseDTO> crear(@Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.status(201).body(empleadoService.guardar(dto)); // 201 con empleado creado
    }

    // PUT /api/empleados/{id} → 200 OK actualizado o 404
    @PutMapping("/{id}") // Mapea PUT /api/empleados/{id}
    public ResponseEntity<EmpleadoResponseDTO> actualizar(
            @PathVariable Long id, // ID del empleado a actualizar
            @Valid @RequestBody EmpleadoRequestDTO dto) { // Body validado con todas las restricciones
        return empleadoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }

    // DELETE /api/empleados/{id} → 204 No Content o 404
    @DeleteMapping("/{id}") // Mapea DELETE /api/empleados/{id}
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (empleadoService.obtenerPorId(id).isEmpty()) { // Verifica si el empleado existe
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
        empleadoService.eliminar(id); // Elimina al empleado del sistema
        return ResponseEntity.noContent().build(); // 204 sin cuerpo de respuesta
    }

    // GET /api/empleados/departamento/RECEPCION → empleados de un departamento
    @GetMapping("/departamento/{departamento}") // Mapea GET /api/empleados/departamento/{nombre}
    public ResponseEntity<List<EmpleadoResponseDTO>> buscarPorDepartamento(@PathVariable String departamento) {
        return ResponseEntity.ok(empleadoService.buscarPorDepartamento(departamento)); // 200 filtrado
    }

    // GET /api/empleados/cargo/Recepcionista → empleados por cargo
    @GetMapping("/cargo/{cargo}") // Mapea GET /api/empleados/cargo/{nombre}
    public ResponseEntity<List<EmpleadoResponseDTO>> buscarPorCargo(@PathVariable String cargo) {
        return ResponseEntity.ok(empleadoService.buscarPorCargo(cargo)); // 200 filtrado por cargo
    }

    // GET /api/empleados/contratacion/2024 → empleados contratados en un año (JPQL)
    @GetMapping("/contratacion/{anio}") // Mapea GET /api/empleados/contratacion/{año}
    public ResponseEntity<List<EmpleadoResponseDTO>> buscarPorAnio(@PathVariable Integer anio) {
        return ResponseEntity.ok(empleadoService.buscarPorAnioContratacion(anio)); // 200 con JPQL
    }

    // GET /api/empleados/estadisticas/por-departamento → distribución del personal (SQL nativo)
    @GetMapping("/estadisticas/por-departamento") // Mapea GET para el organigrama del hotel
    public ResponseEntity<List<Object[]>> obtenerDistribucion() {
        return ResponseEntity.ok(empleadoService.obtenerDistribucionPorDepartamento()); // 200 SQL nativo
    }

}

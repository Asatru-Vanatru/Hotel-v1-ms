// Paquete de la capa de lógica de negocio del microservicio ms-empleados
package com.hotel.empleados.service;

import com.hotel.empleados.dto.EmpleadoRequestDTO; // DTO de entrada para crear/actualizar empleados
import com.hotel.empleados.dto.EmpleadoResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.empleados.model.Empleado; // Entidad JPA de la tabla empleados
import com.hotel.empleados.repository.EmpleadoRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.util.List; // Lista para múltiples empleados
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams

// Capa de negocio: administra el personal del hotel por departamento y cargo
@Service // Spring registra y gestiona este componente de servicio
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository; // Repositorio para la tabla empleados

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private EmpleadoResponseDTO mapToDTO(Empleado e) {
        return new EmpleadoResponseDTO( // Construye el DTO de respuesta desde la entidad
                e.getId(),                  // ID del empleado
                e.getNombre(),              // Nombre del empleado
                e.getApellido(),            // Apellido del empleado
                e.getCargo(),               // Cargo del empleado
                e.getDepartamento(),        // Departamento al que pertenece
                e.getEmail(),               // Email corporativo
                e.getTelefono(),            // Teléfono de contacto
                e.getFechaContratacion(),   // Fecha de contratación
                e.getSalario()              // Salario mensual
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Empleado mapToEntity(EmpleadoRequestDTO dto) {
        return new Empleado( // Construye la entidad desde el DTO validado
                null,                       // null: MySQL genera el id
                dto.getNombre(),            // Nombre desde el DTO
                dto.getApellido(),          // Apellido desde el DTO
                dto.getCargo(),             // Cargo desde el DTO
                dto.getDepartamento(),      // Departamento desde el DTO
                dto.getEmail(),             // Email desde el DTO
                dto.getTelefono(),          // Teléfono desde el DTO
                dto.getFechaContratacion(), // Fecha de contratación desde el DTO
                dto.getSalario()            // Salario desde el DTO
        );
    }

    // Obtiene todos los empleados del hotel
    public List<EmpleadoResponseDTO> obtenerTodos() {
        return empleadoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene un empleado por id
    public Optional<EmpleadoResponseDTO> obtenerPorId(Long id) {
        return empleadoRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Crea un nuevo empleado en el sistema
    public EmpleadoResponseDTO guardar(EmpleadoRequestDTO dto) {
        return mapToDTO(empleadoRepository.save(mapToEntity(dto))); // Guarda y devuelve DTO
    }

    // Actualiza los datos de un empleado existente
    public Optional<EmpleadoResponseDTO> actualizar(Long id, EmpleadoRequestDTO dto) {
        return empleadoRepository.findById(id).map(existente -> { // Busca el empleado existente
            existente.setNombre(dto.getNombre());                         // Actualiza el nombre
            existente.setApellido(dto.getApellido());                     // Actualiza el apellido
            existente.setCargo(dto.getCargo());                           // Actualiza el cargo
            existente.setDepartamento(dto.getDepartamento());             // Actualiza el departamento
            existente.setEmail(dto.getEmail());                           // Actualiza el email
            existente.setTelefono(dto.getTelefono());                     // Actualiza el teléfono
            existente.setFechaContratacion(dto.getFechaContratacion());   // Actualiza la fecha
            existente.setSalario(dto.getSalario());                       // Actualiza el salario
            return mapToDTO(empleadoRepository.save(existente));          // Guarda y retorna DTO
        });
    }

    // Elimina un empleado por id
    public void eliminar(Long id) {
        empleadoRepository.deleteById(id); // Borra el empleado de la base de datos
    }

    // Obtiene empleados de un departamento específico
    public List<EmpleadoResponseDTO> buscarPorDepartamento(String departamento) {
        return empleadoRepository.findByDepartamentoIgnoreCase(departamento).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por departamento
    }

    // Busca empleados por cargo (sin distinción de mayúsculas)
    public List<EmpleadoResponseDTO> buscarPorCargo(String cargo) {
        return empleadoRepository.findByCargoIgnoreCase(cargo).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por cargo
    }

    // Busca empleados contratados en un año específico (JPQL con YEAR)
    public List<EmpleadoResponseDTO> buscarPorAnioContratacion(Integer anio) {
        return empleadoRepository.findByAnioContratacion(anio).stream().map(this::mapToDTO).collect(Collectors.toList()); // JPQL
    }

    // Obtiene la distribución del personal por departamento (SQL nativo con GROUP BY)
    public List<Object[]> obtenerDistribucionPorDepartamento() {
        return empleadoRepository.contarEmpleadosPorDepartamento(); // SQL nativo MySQL
    }

}

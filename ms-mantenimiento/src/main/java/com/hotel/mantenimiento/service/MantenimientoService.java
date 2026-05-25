// Paquete de la capa de lógica de negocio del microservicio ms-mantenimiento
package com.hotel.mantenimiento.service;

import com.hotel.mantenimiento.dto.MantenimientoRequestDTO; // DTO de entrada para crear/actualizar
import com.hotel.mantenimiento.dto.MantenimientoResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.mantenimiento.model.Mantenimiento; // Entidad JPA de la tabla mantenimientos
import com.hotel.mantenimiento.repository.MantenimientoRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección de dependencias
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.time.LocalDate; // Tipo para filtrar por fechas de solicitud
import java.util.List; // Lista para múltiples solicitudes
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams

// Capa de negocio: gestiona solicitudes de reparación y mantenimiento de habitaciones
@Service // Spring registra este componente de servicio en el contexto
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository; // Repositorio para la tabla mantenimientos

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private MantenimientoResponseDTO mapToDTO(Mantenimiento m) {
        return new MantenimientoResponseDTO( // Construye el DTO de respuesta desde la entidad
                m.getId(),                  // ID de la solicitud
                m.getHabitacionId(),        // ID de la habitación
                m.getDescripcion(),         // Descripción del trabajo
                m.getEstado(),              // Estado de la solicitud
                m.getPrioridad(),           // Nivel de urgencia
                m.getEmpleadoId(),          // ID del empleado asignado
                m.getFechaSolicitud(),      // Fecha de la solicitud
                m.getFechaCompletado()      // Fecha de completado (null si en proceso)
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Mantenimiento mapToEntity(MantenimientoRequestDTO dto) {
        return new Mantenimiento( // Construye la entidad desde el DTO validado
                null,                        // null: MySQL genera el id
                dto.getHabitacionId(),       // ID de la habitación que necesita mantenimiento
                dto.getDescripcion(),        // Descripción del trabajo a realizar
                dto.getEstado(),             // Estado inicial de la solicitud
                dto.getPrioridad(),          // Nivel de urgencia asignado
                dto.getEmpleadoId(),         // ID del empleado asignado (puede ser null)
                dto.getFechaSolicitud(),     // Fecha en que se registró la solicitud
                dto.getFechaCompletado()     // Fecha de completado si ya terminó
        );
    }

    // Obtiene todas las solicitudes de mantenimiento
    public List<MantenimientoResponseDTO> obtenerTodos() {
        return mantenimientoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene una solicitud de mantenimiento por id
    public Optional<MantenimientoResponseDTO> obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Registra una nueva solicitud de mantenimiento
    public MantenimientoResponseDTO guardar(MantenimientoRequestDTO dto) {
        return mapToDTO(mantenimientoRepository.save(mapToEntity(dto))); // Guarda y devuelve DTO
    }

    // Actualiza una solicitud de mantenimiento existente
    public Optional<MantenimientoResponseDTO> actualizar(Long id, MantenimientoRequestDTO dto) {
        return mantenimientoRepository.findById(id).map(existente -> { // Busca la solicitud existente
            existente.setHabitacionId(dto.getHabitacionId());           // Actualiza la habitación
            existente.setDescripcion(dto.getDescripcion());             // Actualiza la descripción
            existente.setEstado(dto.getEstado());                       // Actualiza el estado
            existente.setPrioridad(dto.getPrioridad());                 // Actualiza la prioridad
            existente.setEmpleadoId(dto.getEmpleadoId());               // Actualiza el empleado
            existente.setFechaSolicitud(dto.getFechaSolicitud());       // Actualiza la fecha de solicitud
            existente.setFechaCompletado(dto.getFechaCompletado());     // Actualiza la fecha de completado
            return mapToDTO(mantenimientoRepository.save(existente));   // Guarda y retorna DTO
        });
    }

    // Elimina una solicitud de mantenimiento por id
    public void eliminar(Long id) {
        mantenimientoRepository.deleteById(id); // Borra la solicitud de la base de datos
    }

    // Filtra solicitudes por estado (PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO)
    public List<MantenimientoResponseDTO> buscarPorEstado(String estado) {
        return mantenimientoRepository.findByEstado(estado).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por estado
    }

    // Obtiene todas las solicitudes de una habitación específica
    public List<MantenimientoResponseDTO> buscarPorHabitacion(Long habitacionId) {
        return mantenimientoRepository.findByHabitacionId(habitacionId).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por habitación
    }

    // Obtiene solicitudes pendientes urgentes (JPQL con IN)
    public Long contarPendientesUrgentes() {
        return mantenimientoRepository.contarPendientesUrgentes(); // Consulta JPQL
    }

    // Obtiene el ranking de habitaciones más problemáticas (SQL nativo con GROUP BY)
    public List<Object[]> obtenerHabitacionesMasProblematicas() {
        return mantenimientoRepository.findHabitacionesConMasMantenimientos(); // SQL nativo MySQL
    }

}

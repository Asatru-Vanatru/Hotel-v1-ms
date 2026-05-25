// Paquete de la capa de lógica de negocio del microservicio ms-servicios
package com.hotel.servicios.service;

import com.hotel.servicios.dto.ServicioRequestDTO; // DTO de entrada para crear/actualizar servicios
import com.hotel.servicios.dto.ServicioResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.servicios.model.Servicio; // Entidad JPA de la tabla servicios
import com.hotel.servicios.repository.ServicioRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección de dependencias
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.math.BigDecimal; // Tipo para el precio máximo en la búsqueda por precio
import java.util.List; // Lista para el catálogo de servicios
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams en listas

// Capa de negocio: administra el catálogo de servicios adicionales del hotel
@Service // Spring registra y gestiona este componente
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class ServicioService {

    private final ServicioRepository servicioRepository; // Repositorio para la tabla servicios

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private ServicioResponseDTO mapToDTO(Servicio s) {
        return new ServicioResponseDTO( // Construye el DTO de respuesta desde la entidad
                s.getId(),          // ID del servicio
                s.getNombre(),      // Nombre del servicio
                s.getDescripcion(), // Descripción del servicio
                s.getPrecio(),      // Precio del servicio
                s.getCategoria(),   // Categoría del servicio
                s.getDisponible()   // Estado de disponibilidad
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Servicio mapToEntity(ServicioRequestDTO dto) {
        return new Servicio( // Construye la entidad desde el DTO validado
                null,                  // null: MySQL genera el id
                dto.getNombre(),       // Nombre desde el DTO
                dto.getDescripcion(),  // Descripción desde el DTO
                dto.getPrecio(),       // Precio desde el DTO
                dto.getCategoria(),    // Categoría desde el DTO
                dto.getDisponible()    // Disponibilidad desde el DTO
        );
    }

    // Obtiene todo el catálogo de servicios
    public List<ServicioResponseDTO> obtenerTodos() {
        return servicioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Catálogo completo
    }

    // Obtiene un servicio por id
    public Optional<ServicioResponseDTO> obtenerPorId(Long id) {
        return servicioRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Crea un nuevo servicio en el catálogo
    public ServicioResponseDTO guardar(ServicioRequestDTO dto) {
        return mapToDTO(servicioRepository.save(mapToEntity(dto))); // Guarda y devuelve DTO
    }

    // Actualiza un servicio existente
    public Optional<ServicioResponseDTO> actualizar(Long id, ServicioRequestDTO dto) {
        return servicioRepository.findById(id).map(existente -> { // Busca el servicio existente
            existente.setNombre(dto.getNombre());           // Actualiza el nombre
            existente.setDescripcion(dto.getDescripcion()); // Actualiza la descripción
            existente.setPrecio(dto.getPrecio());           // Actualiza el precio
            existente.setCategoria(dto.getCategoria());     // Actualiza la categoría
            existente.setDisponible(dto.getDisponible());   // Actualiza la disponibilidad
            return mapToDTO(servicioRepository.save(existente)); // Guarda y retorna DTO
        });
    }

    // Elimina un servicio del catálogo por id
    public void eliminar(Long id) {
        servicioRepository.deleteById(id); // Borra el servicio de la base de datos
    }

    // Obtiene solo los servicios disponibles del catálogo activo
    public List<ServicioResponseDTO> buscarPorDisponibilidad(Boolean disponible) {
        return servicioRepository.findByDisponible(disponible).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por disponibilidad
    }

    // Filtra el catálogo por categoría de servicio
    public List<ServicioResponseDTO> buscarPorCategoria(String categoria) {
        return servicioRepository.findByCategoriaIgnoreCase(categoria).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por categoría
    }

    // Busca servicios disponibles hasta un precio máximo (JPQL)
    public List<ServicioResponseDTO> buscarHastaPrecio(BigDecimal precioMax) {
        return servicioRepository.findDisponiblesHastaPreio(precioMax).stream().map(this::mapToDTO).collect(Collectors.toList()); // JPQL
    }

    // Obtiene el catálogo disponible ordenado por categoría y precio (SQL nativo)
    public List<ServicioResponseDTO> obtenerCatalogoOrdenado() {
        return servicioRepository.findCatalogoDisponibleOrdenado().stream().map(this::mapToDTO).collect(Collectors.toList()); // SQL nativo
    }

}

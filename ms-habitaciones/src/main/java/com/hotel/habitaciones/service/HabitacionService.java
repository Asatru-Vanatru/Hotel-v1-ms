// Paquete de la capa de lógica de negocio del microservicio ms-habitaciones
package com.hotel.habitaciones.service;

import com.hotel.habitaciones.dto.HabitacionRequestDTO; // DTO de entrada para crear/actualizar
import com.hotel.habitaciones.dto.HabitacionResponseDTO; // DTO de salida para respuestas HTTP
import com.hotel.habitaciones.model.Habitacion; // Entidad JPA de la tabla habitaciones
import com.hotel.habitaciones.repository.HabitacionRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección de dependencias
import org.springframework.stereotype.Service; // Marca esta clase como servicio Spring
import java.math.BigDecimal; // Tipo para el rango de precios en la búsqueda
import java.util.List; // Lista para múltiples resultados
import java.util.Optional; // Encapsula resultado que puede o no existir
import java.util.stream.Collectors; // Para coleccionar Streams en listas

// Capa de lógica de negocio: gestiona el catálogo de habitaciones del hotel
@Service // Spring registra este bean en el contenedor de aplicación
@RequiredArgsConstructor // Lombok genera el constructor con el repositorio inyectado
public class HabitacionService {

    private final HabitacionRepository habitacionRepository; // Acceso a datos de habitaciones

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private HabitacionResponseDTO mapToDTO(Habitacion h) {
        return new HabitacionResponseDTO( // Construye el DTO de respuesta desde la entidad
                h.getId(),          // ID generado por MySQL
                h.getNumero(),      // Número de habitación
                h.getTipo(),        // Tipo de habitación
                h.getCapacidad(),   // Capacidad máxima
                h.getPrecioNoche(), // Precio por noche
                h.getDescripcion(), // Descripción de amenidades
                h.getEstado(),      // Estado actual
                h.getPiso()         // Número de piso
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Habitacion mapToEntity(HabitacionRequestDTO dto) {
        return new Habitacion( // Construye la entidad desde el DTO validado
                null,                  // null: MySQL generará el id con AUTO_INCREMENT
                dto.getNumero(),       // Número de habitación desde el DTO
                dto.getTipo(),         // Tipo de habitación desde el DTO
                dto.getCapacidad(),    // Capacidad desde el DTO
                dto.getPrecioNoche(),  // Precio por noche desde el DTO
                dto.getDescripcion(),  // Descripción desde el DTO
                dto.getEstado(),       // Estado desde el DTO
                dto.getPiso()          // Piso desde el DTO
        );
    }

    // Obtiene todas las habitaciones del catálogo
    public List<HabitacionResponseDTO> obtenerTodas() {
        return habitacionRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Busca una habitación por id, retorna Optional para manejo de 404 en el Controller
    public Optional<HabitacionResponseDTO> obtenerPorId(Long id) {
        return habitacionRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Crea una nueva habitación en el catálogo del hotel
    public HabitacionResponseDTO guardar(HabitacionRequestDTO dto) {
        return mapToDTO(habitacionRepository.save(mapToEntity(dto))); // Guarda entidad y devuelve DTO
    }

    // Actualiza una habitación existente por id
    public Optional<HabitacionResponseDTO> actualizar(Long id, HabitacionRequestDTO dto) {
        return habitacionRepository.findById(id).map(existente -> { // Busca la habitación existente
            existente.setNumero(dto.getNumero());           // Actualiza el número de habitación
            existente.setTipo(dto.getTipo());               // Actualiza el tipo de habitación
            existente.setCapacidad(dto.getCapacidad());     // Actualiza la capacidad
            existente.setPrecioNoche(dto.getPrecioNoche()); // Actualiza el precio por noche
            existente.setDescripcion(dto.getDescripcion()); // Actualiza la descripción
            existente.setEstado(dto.getEstado());           // Actualiza el estado
            existente.setPiso(dto.getPiso());               // Actualiza el número de piso
            return mapToDTO(habitacionRepository.save(existente)); // Guarda y retorna DTO
        });
    }

    // Elimina una habitación del catálogo por su id
    public void eliminar(Long id) {
        habitacionRepository.deleteById(id); // Borra la habitación de la base de datos
    }

    // ── SOPORTE HATEOAS / ENTIDADES ───────────────────────────────────────────
    public List<Habitacion> obtenerTodasEntidades() {
        return habitacionRepository.findAll();
    }

    public Optional<Habitacion> obtenerEntidadPorId(Long id) {
        return habitacionRepository.findById(id);
    }

    public Habitacion guardarEntidad(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public void eliminarEntidad(Long id) {
        habitacionRepository.deleteById(id);
    }

    // Busca habitaciones por estado (DISPONIBLE, OCUPADA, MANTENIMIENTO, etc.)
    public List<HabitacionResponseDTO> buscarPorEstado(String estado) {
        return habitacionRepository.findByEstado(estado).stream().map(this::mapToDTO).collect(Collectors.toList()); // Filtra por estado
    }

    // Busca habitaciones por tipo (SIMPLE, DOBLE, SUITE, FAMILIAR, etc.)
    public List<HabitacionResponseDTO> buscarPorTipo(String tipo) {
        return habitacionRepository.findByTipo(tipo).stream().map(this::mapToDTO).collect(Collectors.toList()); // Filtra por tipo
    }

    // Busca habitaciones disponibles de un tipo específico (JPQL)
    public List<HabitacionResponseDTO> buscarDisponiblesPorTipo(String tipo) {
        return habitacionRepository.findDisponiblesPorTipo(tipo).stream().map(this::mapToDTO).collect(Collectors.toList()); // JPQL
    }

    // Busca habitaciones dentro de un rango de precio por noche (JPQL con BETWEEN)
    public List<HabitacionResponseDTO> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return habitacionRepository.findByRangoPrecio(min, max).stream().map(this::mapToDTO).collect(Collectors.toList()); // JPQL rango
    }

    // Obtiene todas las habitaciones disponibles ordenadas por precio (SQL nativo)
    public List<HabitacionResponseDTO> obtenerDisponiblesOrdenadas() {
        return habitacionRepository.findTodasDisponiblesOrdenadas().stream().map(this::mapToDTO).collect(Collectors.toList()); // SQL nativo
    }

}

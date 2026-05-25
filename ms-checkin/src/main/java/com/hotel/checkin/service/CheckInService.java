// Paquete de la capa de lógica de negocio del microservicio ms-checkin
package com.hotel.checkin.service;

import com.hotel.checkin.dto.CheckInRequestDTO; // DTO de entrada para registrar el check-in
import com.hotel.checkin.dto.CheckInResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.checkin.model.CheckIn; // Entidad JPA de la tabla checkins
import com.hotel.checkin.repository.CheckInRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.util.List; // Lista para múltiples check-ins
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams

// Capa de negocio: registra y gestiona el proceso de entrada de huéspedes al hotel
@Service // Spring gestiona este bean como componente de servicio
@RequiredArgsConstructor // Lombok genera el constructor con el repositorio inyectado
public class CheckInService {

    private final CheckInRepository checkInRepository; // Repositorio para operar sobre la tabla checkins

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private CheckInResponseDTO mapToDTO(CheckIn c) {
        return new CheckInResponseDTO( // Crea DTO de respuesta desde la entidad
                c.getId(),                  // ID del check-in
                c.getReservaId(),           // ID de la reserva
                c.getClienteId(),           // ID del cliente
                c.getHabitacionId(),        // ID de la habitación
                c.getFechaHoraCheckIn(),    // Momento del check-in
                c.getObservaciones()        // Observaciones del recepcionista
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private CheckIn mapToEntity(CheckInRequestDTO dto) {
        return new CheckIn( // Crea la entidad desde el DTO validado
                null,                        // null: MySQL genera el id
                dto.getReservaId(),          // ID de la reserva activa
                dto.getClienteId(),          // ID del cliente que hace check-in
                dto.getHabitacionId(),       // ID de la habitación asignada
                dto.getFechaHoraCheckIn(),   // Momento del check-in
                dto.getObservaciones()       // Observaciones del recepcionista
        );
    }

    // Obtiene todos los registros de check-in
    public List<CheckInResponseDTO> obtenerTodos() {
        return checkInRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene un check-in por id
    public Optional<CheckInResponseDTO> obtenerPorId(Long id) {
        return checkInRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Registra un nuevo check-in en el sistema
    public CheckInResponseDTO registrar(CheckInRequestDTO dto) {
        return mapToDTO(checkInRepository.save(mapToEntity(dto))); // Guarda el check-in y retorna DTO
    }

    // Actualiza un check-in existente (por ejemplo, para corregir observaciones)
    public Optional<CheckInResponseDTO> actualizar(Long id, CheckInRequestDTO dto) {
        return checkInRepository.findById(id).map(existente -> { // Busca el check-in existente
            existente.setReservaId(dto.getReservaId());                   // Actualiza la reserva
            existente.setClienteId(dto.getClienteId());                   // Actualiza el cliente
            existente.setHabitacionId(dto.getHabitacionId());             // Actualiza la habitación
            existente.setFechaHoraCheckIn(dto.getFechaHoraCheckIn());     // Actualiza el momento
            existente.setObservaciones(dto.getObservaciones());           // Actualiza las observaciones
            return mapToDTO(checkInRepository.save(existente));           // Guarda y retorna DTO
        });
    }

    // Elimina un check-in por id
    public void eliminar(Long id) {
        checkInRepository.deleteById(id); // Borra el registro de check-in de la base de datos
    }

    // Busca el check-in de una reserva específica
    public Optional<CheckInResponseDTO> buscarPorReserva(Long reservaId) {
        return checkInRepository.findByReservaId(reservaId).map(this::mapToDTO); // Query Method del repositorio
    }

    // Obtiene el historial de check-ins de un cliente
    public List<CheckInResponseDTO> buscarPorCliente(Long clienteId) {
        return checkInRepository.findByClienteId(clienteId).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por cliente
    }

    // Obtiene los check-ins registrados hoy (JPQL)
    public List<CheckInResponseDTO> obtenerCheckInsDeHoy() {
        return checkInRepository.findCheckInsDeHoy().stream().map(this::mapToDTO).collect(Collectors.toList()); // JPQL de hoy
    }

    // Cuenta los check-ins del último mes (SQL nativo para estadísticas)
    public Long contarCheckInsUltimoMes() {
        return checkInRepository.contarCheckInsUltimoMes(); // SQL nativo MySQL
    }

}

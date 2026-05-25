// Paquete de la capa de lógica de negocio del microservicio ms-checkout
package com.hotel.checkout.service;

import com.hotel.checkout.dto.CheckOutRequestDTO; // DTO de entrada para registrar el check-out
import com.hotel.checkout.dto.CheckOutResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.checkout.model.CheckOut; // Entidad JPA de la tabla checkouts
import com.hotel.checkout.repository.CheckOutRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.math.BigDecimal; // Tipo para totales monetarios
import java.util.List; // Lista para múltiples resultados
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams

// Capa de negocio: gestiona el cierre de estadía y el cobro final al huésped
@Service // Spring gestiona este componente de servicio
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class CheckOutService {

    private final CheckOutRepository checkOutRepository; // Repositorio para la tabla checkouts

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private CheckOutResponseDTO mapToDTO(CheckOut c) {
        return new CheckOutResponseDTO( // Construye el DTO de respuesta desde la entidad
                c.getId(),                  // ID del check-out
                c.getReservaId(),           // ID de la reserva cerrada
                c.getClienteId(),           // ID del cliente
                c.getHabitacionId(),        // ID de la habitación liberada
                c.getFechaHoraCheckOut(),   // Momento del check-out
                c.getTotalFinal(),          // Total cobrado al huésped
                c.getDiasEstancia()         // Días de estadía
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private CheckOut mapToEntity(CheckOutRequestDTO dto) {
        return new CheckOut( // Construye la entidad desde el DTO validado
                null,                         // null: MySQL genera el id
                dto.getReservaId(),           // ID de la reserva que se cierra
                dto.getClienteId(),           // ID del cliente que hace check-out
                dto.getHabitacionId(),        // ID de la habitación que se libera
                dto.getFechaHoraCheckOut(),   // Momento del check-out
                dto.getTotalFinal(),          // Total final cobrado
                dto.getDiasEstancia()         // Días de estadía
        );
    }

    // Obtiene todos los registros de check-out
    public List<CheckOutResponseDTO> obtenerTodos() {
        return checkOutRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene un check-out por id
    public Optional<CheckOutResponseDTO> obtenerPorId(Long id) {
        return checkOutRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Registra el proceso de check-out de un huésped
    public CheckOutResponseDTO registrar(CheckOutRequestDTO dto) {
        return mapToDTO(checkOutRepository.save(mapToEntity(dto))); // Guarda el check-out y retorna DTO
    }

    // Actualiza un check-out existente
    public Optional<CheckOutResponseDTO> actualizar(Long id, CheckOutRequestDTO dto) {
        return checkOutRepository.findById(id).map(existente -> { // Busca el check-out existente
            existente.setReservaId(dto.getReservaId());                     // Actualiza la reserva
            existente.setClienteId(dto.getClienteId());                     // Actualiza el cliente
            existente.setHabitacionId(dto.getHabitacionId());               // Actualiza la habitación
            existente.setFechaHoraCheckOut(dto.getFechaHoraCheckOut());     // Actualiza el momento
            existente.setTotalFinal(dto.getTotalFinal());                   // Actualiza el total
            existente.setDiasEstancia(dto.getDiasEstancia());               // Actualiza los días
            return mapToDTO(checkOutRepository.save(existente));            // Guarda y retorna DTO
        });
    }

    // Elimina un check-out por id
    public void eliminar(Long id) {
        checkOutRepository.deleteById(id); // Borra el registro de check-out
    }

    // Busca el check-out de una reserva específica
    public Optional<CheckOutResponseDTO> buscarPorReserva(Long reservaId) {
        return checkOutRepository.findByReservaId(reservaId).map(this::mapToDTO); // Query Method
    }

    // Calcula el total de ingresos de todos los check-outs (JPQL con SUM)
    public BigDecimal calcularIngresosTotales() {
        return checkOutRepository.calcularIngresosTotales(); // Consulta JPQL de suma total
    }

    // Calcula el promedio de días de estadía (SQL nativo con AVG de MySQL)
    public Double calcularPromedioEstancia() {
        return checkOutRepository.calcularPromedioEstancia(); // SQL nativo con AVG
    }

}

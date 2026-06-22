// Paquete de la capa de lógica de negocio del microservicio ms-consumos
package com.hotel.consumos.service;

import com.hotel.consumos.dto.ConsumoRequestDTO; // DTO de entrada para registrar consumos
import com.hotel.consumos.dto.ConsumoResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.consumos.model.Consumo; // Entidad JPA de la tabla consumos
import com.hotel.consumos.repository.ConsumoRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.math.BigDecimal; // Tipo para el total de consumos en cálculos agregados
import java.util.List; // Lista para múltiples consumos
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams en listas

// Capa de negocio: registra y calcula los consumos de servicios adicionales por reserva
@Service // Spring registra este componente de servicio
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class ConsumoService {

    private final ConsumoRepository consumoRepository; // Repositorio para la tabla consumos

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private ConsumoResponseDTO mapToDTO(Consumo c) {
        return new ConsumoResponseDTO( // Construye el DTO de respuesta desde la entidad
                c.getId(),            // ID del consumo
                c.getReservaId(),     // ID de la reserva
                c.getClienteId(),     // ID del cliente
                c.getServicioId(),    // ID del servicio consumido
                c.getCantidad(),      // Cantidad consumida
                c.getFechaConsumo(),  // Momento del consumo
                c.getSubtotal(),      // Subtotal calculado
                c.getObservaciones()  // Observaciones del consumo
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Consumo mapToEntity(ConsumoRequestDTO dto) {
        return new Consumo( // Construye la entidad desde el DTO validado
                null,                    // null: MySQL genera el id
                dto.getReservaId(),      // ID de la reserva
                dto.getClienteId(),      // ID del cliente
                dto.getServicioId(),     // ID del servicio consumido
                dto.getCantidad(),       // Cantidad de unidades consumidas
                dto.getFechaConsumo(),   // Momento del consumo
                dto.getSubtotal(),       // Subtotal pre-calculado
                dto.getObservaciones()   // Observaciones opcionales
        );
    }

    // Obtiene todos los registros de consumo
    public List<ConsumoResponseDTO> obtenerTodos() {
        return consumoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene un consumo por id
    public Optional<ConsumoResponseDTO> obtenerPorId(Long id) {
        return consumoRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Registra un nuevo consumo de servicio adicional durante una reserva
    public ConsumoResponseDTO registrar(ConsumoRequestDTO dto) {
        return mapToDTO(consumoRepository.save(mapToEntity(dto))); // Guarda y devuelve DTO
    }

    // Actualiza un consumo existente
    public Optional<ConsumoResponseDTO> actualizar(Long id, ConsumoRequestDTO dto) {
        return consumoRepository.findById(id).map(existente -> { // Busca el consumo existente
            existente.setReservaId(dto.getReservaId());           // Actualiza la reserva
            existente.setClienteId(dto.getClienteId());           // Actualiza el cliente
            existente.setServicioId(dto.getServicioId());         // Actualiza el servicio
            existente.setCantidad(dto.getCantidad());             // Actualiza la cantidad
            existente.setFechaConsumo(dto.getFechaConsumo());     // Actualiza el momento
            existente.setSubtotal(dto.getSubtotal());             // Actualiza el subtotal
            existente.setObservaciones(dto.getObservaciones());   // Actualiza las observaciones
            return mapToDTO(consumoRepository.save(existente));   // Guarda y retorna DTO
        });
    }

    // Elimina un consumo por id
    public void eliminar(Long id) {
        consumoRepository.deleteById(id); // Borra el registro de consumo de la base de datos
    }

    // ── SOPORTE HATEOAS / ENTIDADES ───────────────────────────────────────────
    public List<Consumo> obtenerTodasEntidades() {
        return consumoRepository.findAll();
    }

    public Optional<Consumo> obtenerEntidadPorId(Long id) {
        return consumoRepository.findById(id);
    }

    public Consumo guardarEntidad(Consumo consumo) {
        return consumoRepository.save(consumo);
    }

    public void eliminarEntidad(Long id) {
        consumoRepository.deleteById(id);
    }

    // Obtiene todos los consumos de una reserva específica
    public List<ConsumoResponseDTO> buscarPorReserva(Long reservaId) {
        return consumoRepository.findByReservaId(reservaId).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por reserva
    }

    // Calcula el total de consumos de una reserva (JPQL con SUM)
    public BigDecimal calcularTotalPorReserva(Long reservaId) {
        return consumoRepository.calcularTotalConsumosPorReserva(reservaId); // Consulta JPQL de suma
    }

    // Obtiene el ranking de los servicios más consumidos (SQL nativo con GROUP BY)
    public List<Object[]> obtenerServiciosMasConsumidos() {
        return consumoRepository.findServiciosMasConsumidos(); // SQL nativo MySQL
    }

}

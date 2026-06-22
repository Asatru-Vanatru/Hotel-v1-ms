// Paquete de la capa de lógica de negocio del microservicio ms-pagos
package com.hotel.pagos.service;

import com.hotel.pagos.dto.PagoRequestDTO; // DTO de entrada para registrar pagos
import com.hotel.pagos.dto.PagoResponseDTO; // DTO de salida para la respuesta HTTP
import com.hotel.pagos.model.Pago; // Entidad JPA de la tabla pagos
import com.hotel.pagos.repository.PagoRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Genera constructor de inyección
import org.springframework.stereotype.Service; // Marca como componente de servicio Spring
import java.math.BigDecimal; // Tipo para totales monetarios
import java.util.List; // Lista para múltiples pagos
import java.util.Optional; // Encapsula resultado que puede no existir
import java.util.stream.Collectors; // Para coleccionar Streams

// Capa de negocio: procesa y registra las transacciones de pago de las reservas
@Service // Spring gestiona este componente de servicio
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class PagoService {

    private final PagoRepository pagoRepository; // Repositorio para la tabla pagos

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private PagoResponseDTO mapToDTO(Pago p) {
        return new PagoResponseDTO( // Construye el DTO de respuesta desde la entidad
                p.getId(),           // ID del pago
                p.getReservaId(),    // ID de la reserva
                p.getClienteId(),    // ID del cliente
                p.getMonto(),        // Monto pagado
                p.getMetodoPago(),   // Método de pago utilizado
                p.getEstado(),       // Estado del pago
                p.getFechaPago(),    // Momento del pago
                p.getReferencia()    // Referencia del comprobante
        );
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Pago mapToEntity(PagoRequestDTO dto) {
        return new Pago( // Construye la entidad desde el DTO validado
                null,                   // null: MySQL genera el id
                dto.getReservaId(),     // ID de la reserva que se paga
                dto.getClienteId(),     // ID del cliente que paga
                dto.getMonto(),         // Monto de la transacción
                dto.getMetodoPago(),    // Forma de pago elegida
                dto.getEstado(),        // Estado inicial del pago
                dto.getFechaPago(),     // Momento de la transacción
                dto.getReferencia()     // Referencia del comprobante
        );
    }

    // Obtiene todos los registros de pago
    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene un pago por id
    public Optional<PagoResponseDTO> obtenerPorId(Long id) {
        return pagoRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Registra una nueva transacción de pago
    public PagoResponseDTO registrar(PagoRequestDTO dto) {
        return mapToDTO(pagoRepository.save(mapToEntity(dto))); // Guarda el pago y retorna DTO
    }

    // Actualiza un pago existente (ej: cambiar estado de PENDIENTE a COMPLETADO)
    public Optional<PagoResponseDTO> actualizar(Long id, PagoRequestDTO dto) {
        return pagoRepository.findById(id).map(existente -> { // Busca el pago existente
            existente.setReservaId(dto.getReservaId());       // Actualiza la reserva
            existente.setClienteId(dto.getClienteId());       // Actualiza el cliente
            existente.setMonto(dto.getMonto());               // Actualiza el monto
            existente.setMetodoPago(dto.getMetodoPago());     // Actualiza el método de pago
            existente.setEstado(dto.getEstado());             // Actualiza el estado del pago
            existente.setFechaPago(dto.getFechaPago());       // Actualiza la fecha del pago
            existente.setReferencia(dto.getReferencia());     // Actualiza la referencia
            return mapToDTO(pagoRepository.save(existente)); // Guarda y retorna DTO
        });
    }

    // Elimina un pago por id
    public void eliminar(Long id) {
        pagoRepository.deleteById(id); // Borra el registro de pago de la base de datos
    }

    // ── SOPORTE HATEOAS / ENTIDADES ───────────────────────────────────────────
    public List<Pago> obtenerTodasEntidades() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> obtenerEntidadPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago guardarEntidad(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminarEntidad(Long id) {
        pagoRepository.deleteById(id);
    }

    // Obtiene todos los pagos de una reserva específica
    public List<PagoResponseDTO> buscarPorReserva(Long reservaId) {
        return pagoRepository.findByReservaId(reservaId).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por reserva
    }

    // Filtra los pagos por estado (PENDIENTE, COMPLETADO, CANCELADO, etc.)
    public List<PagoResponseDTO> buscarPorEstado(String estado) {
        return pagoRepository.findByEstado(estado).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por estado
    }

    // Calcula el total pagado de una reserva (JPQL con SUM)
    public BigDecimal calcularTotalPorReserva(Long reservaId) {
        return pagoRepository.calcularTotalPagadoPorReserva(reservaId); // Consulta JPQL de suma
    }

    // Obtiene el reporte de ingresos por método de pago (SQL nativo con GROUP BY)
    public List<Object[]> obtenerIngresosPorMetodo() {
        return pagoRepository.calcularIngresosPorMetodoPago(); // SQL nativo MySQL para reporte
    }

}

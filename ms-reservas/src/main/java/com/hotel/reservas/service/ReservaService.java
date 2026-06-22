// Paquete de la capa de lógica de negocio del microservicio ms-reservas
package com.hotel.reservas.service;

import com.hotel.reservas.dto.ReservaRequestDTO; // DTO de entrada para crear/actualizar reservas
import com.hotel.reservas.dto.ReservaResponseDTO; // DTO de salida para respuestas HTTP
import com.hotel.reservas.model.Reserva; // Entidad JPA de la tabla reservas
import com.hotel.reservas.repository.ReservaRepository; // Repositorio de acceso a datos
import lombok.RequiredArgsConstructor; // Lombok: genera constructor de inyección de dependencias
import org.springframework.stereotype.Service; // Marca esta clase como componente de servicio Spring
import java.math.BigDecimal; // Tipo para el cálculo del total estimado de la reserva
import java.time.LocalDate; // Tipo para las fechas de filtro en búsquedas
import java.time.temporal.ChronoUnit; // Para calcular días entre fechas (ingreso a salida)
import java.util.List; // Lista para múltiples reservas
import java.util.Optional; // Encapsula resultado que puede o no existir
import java.util.stream.Collectors; // Para coleccionar Streams en listas

// Capa de negocio: gestiona reservas de habitaciones con lógica de cálculo de totales
@Service // Spring gestiona este componente en el contexto de la aplicación
@RequiredArgsConstructor // Lombok inyecta el repositorio por constructor
public class ReservaService {

    private final ReservaRepository reservaRepository; // Repositorio para operar sobre la tabla reservas

    // ── MAPEO: Entidad → ResponseDTO ────────────────────────────────────────
    private ReservaResponseDTO mapToDTO(Reserva r) {
        return new ReservaResponseDTO( // Construye el DTO de respuesta desde la entidad
                r.getId(),              // ID de la reserva
                r.getClienteId(),       // ID del cliente
                r.getHabitacionId(),    // ID de la habitación
                r.getFechaIngreso(),    // Fecha de llegada
                r.getFechaSalida(),     // Fecha de salida
                r.getEstado(),          // Estado actual de la reserva
                r.getTotalEstimado(),   // Total estimado calculado
                r.getNumeroPasajeros()  // Número de pasajeros
        );
    }

    // ── LÓGICA DE NEGOCIO: calcular total estimado ────────────────────────
    // El total se calcula como: precio_noche × número_de_noches
    // NOTA: En un sistema real, el precio se obtendría de ms-habitaciones vía HTTP
    // Aquí usamos un precio base para demostrar la lógica de negocio
    private BigDecimal calcularTotalEstimado(LocalDate ingreso, LocalDate salida, BigDecimal precioBase) {
        long noches = ChronoUnit.DAYS.between(ingreso, salida); // Calcula diferencia en días entre fechas
        return precioBase.multiply(BigDecimal.valueOf(noches)); // Multiplica precio base por noches
    }

    // ── MAPEO: RequestDTO → Entidad ──────────────────────────────────────────
    private Reserva mapToEntity(ReservaRequestDTO dto) {
        BigDecimal precioBase = new BigDecimal("100.00"); // Precio base por noche (referencial)
        BigDecimal total = calcularTotalEstimado(dto.getFechaIngreso(), dto.getFechaSalida(), precioBase); // Calcula total
        return new Reserva( // Construye la entidad desde el DTO validado
                null,                    // null: MySQL genera el id
                dto.getClienteId(),      // ID del cliente de la reserva
                dto.getHabitacionId(),   // ID de la habitación reservada
                dto.getFechaIngreso(),   // Fecha de llegada del huésped
                dto.getFechaSalida(),    // Fecha de salida del huésped
                dto.getEstado(),         // Estado inicial de la reserva
                total,                   // Total calculado por la lógica de negocio
                dto.getNumeroPasajeros() // Número de pasajeros de la reserva
        );
    }

    // Obtiene todas las reservas del sistema
    public List<ReservaResponseDTO> obtenerTodas() {
        return reservaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); // Lista completa
    }

    // Obtiene una reserva por id, retorna Optional para manejo de 404
    public Optional<ReservaResponseDTO> obtenerPorId(Long id) {
        return reservaRepository.findById(id).map(this::mapToDTO); // Busca y mapea si existe
    }

    // Crea una nueva reserva calculando el total estimado
    public ReservaResponseDTO guardar(ReservaRequestDTO dto) {
        return mapToDTO(reservaRepository.save(mapToEntity(dto))); // Guarda y devuelve DTO con total calculado
    }

    // Actualiza una reserva existente
    public Optional<ReservaResponseDTO> actualizar(Long id, ReservaRequestDTO dto) {
        return reservaRepository.findById(id).map(existente -> { // Busca la reserva existente
            BigDecimal total = calcularTotalEstimado(dto.getFechaIngreso(), dto.getFechaSalida(), new BigDecimal("100.00")); // Recalcula total
            existente.setClienteId(dto.getClienteId());           // Actualiza el cliente
            existente.setHabitacionId(dto.getHabitacionId());     // Actualiza la habitación
            existente.setFechaIngreso(dto.getFechaIngreso());     // Actualiza fecha de ingreso
            existente.setFechaSalida(dto.getFechaSalida());       // Actualiza fecha de salida
            existente.setEstado(dto.getEstado());                 // Actualiza el estado
            existente.setTotalEstimado(total);                   // Actualiza el total recalculado
            existente.setNumeroPasajeros(dto.getNumeroPasajeros()); // Actualiza pasajeros
            return mapToDTO(reservaRepository.save(existente));  // Guarda y retorna DTO
        });
    }

    // Elimina una reserva por id
    public void eliminar(Long id) {
        reservaRepository.deleteById(id); // Borra la reserva de la base de datos
    }

    // ── SOPORTE HATEOAS / ENTIDADES ───────────────────────────────────────────
    public List<Reserva> obtenerTodasEntidades() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerEntidadPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva guardarEntidad(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public void eliminarEntidad(Long id) {
        reservaRepository.deleteById(id);
    }

    // Obtiene reservas de un cliente específico
    public List<ReservaResponseDTO> buscarPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId).stream().map(this::mapToDTO).collect(Collectors.toList()); // Historial del cliente
    }

    // Obtiene reservas filtradas por estado
    public List<ReservaResponseDTO> buscarPorEstado(String estado) {
        return reservaRepository.findByEstado(estado).stream().map(this::mapToDTO).collect(Collectors.toList()); // Por estado
    }

    // Obtiene las próximas llegadas confirmadas (JPQL con ORDER BY)
    public List<ReservaResponseDTO> obtenerProximasLlegadas() {
        return reservaRepository.findProximasLlegadas().stream().map(this::mapToDTO).collect(Collectors.toList()); // Próximas llegadas
    }

    // Calcula el total de noches completadas de un cliente (SQL nativo)
    public Long calcularNochesCliente(Long clienteId) {
        return reservaRepository.calcularTotalNochesCliente(clienteId); // SQL nativo con SUM y DATEDIFF
    }

}

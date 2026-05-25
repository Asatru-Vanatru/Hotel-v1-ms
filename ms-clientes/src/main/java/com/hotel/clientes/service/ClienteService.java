// Paquete de la capa de lógica de negocio del microservicio ms-clientes
package com.hotel.clientes.service;

// Importa las clases del mismo microservicio (DTOs, modelo y repositorio)
import com.hotel.clientes.dto.ClienteRequestDTO;
import com.hotel.clientes.dto.ClienteResponseDTO;
import com.hotel.clientes.model.Cliente;
import com.hotel.clientes.repository.ClienteRepository;
// @RequiredArgsConstructor: Lombok genera el constructor de inyección de dependencias
import lombok.RequiredArgsConstructor;
// @Service: marca esta clase como componente de lógica de negocio en el contenedor Spring
import org.springframework.stereotype.Service;

// Importa las colecciones y Optional de Java
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Permite coleccionar Stream en listas

// ═══════════════════════════════════════════════════════════
// ClienteService.java
// Capa de lógica de negocio del microservicio ms-clientes.
// Actúa como intermediario entre el Controller y el Repository.
// Aplica reglas de negocio y transforma entre entidades y DTOs.
// ═══════════════════════════════════════════════════════════

// @Service: Spring registra esta clase como bean de servicio en el contexto de la aplicación
@Service
// @RequiredArgsConstructor: genera constructor con todos los campos 'final' para inyección
@RequiredArgsConstructor
public class ClienteService {

    // 'final' + @RequiredArgsConstructor → Spring inyecta el repositorio por constructor
    private final ClienteRepository clienteRepository; // Repositorio de acceso a datos de clientes

    // ── MAPEO PRIVADO: Entidad → ResponseDTO ────────────────────────────────
    // Solo este Service usa este método de mapeo.
    // El Controller no conoce la entidad, el Repository no conoce el DTO.
    private ClienteResponseDTO mapToDTO(Cliente cliente) {
        return new ClienteResponseDTO( // Crea un nuevo DTO de respuesta con los datos de la entidad
                cliente.getId(),              // Copia el id generado por MySQL
                cliente.getNombre(),          // Copia el nombre del cliente
                cliente.getApellido(),        // Copia el apellido del cliente
                cliente.getEmail(),           // Copia el email del cliente
                cliente.getTelefono(),        // Copia el teléfono del cliente
                cliente.getTipoDocumento(),   // Copia el tipo de documento
                cliente.getNumeroDocumento(), // Copia el número de documento
                cliente.getNacionalidad()     // Copia la nacionalidad del cliente
        );
    }

    // ── MAPEO PRIVADO: RequestDTO → Entidad ─────────────────────────────────
    // Convierte el DTO de entrada en una entidad lista para guardar en MySQL
    private Cliente mapToEntity(ClienteRequestDTO dto) {
        return new Cliente( // Crea una nueva entidad con los datos del DTO
                null,                    // null: MySQL generará el id automáticamente
                dto.getNombre(),         // Nombre desde el DTO validado
                dto.getApellido(),       // Apellido desde el DTO validado
                dto.getEmail(),          // Email desde el DTO validado
                dto.getTelefono(),       // Teléfono desde el DTO
                dto.getTipoDocumento(),  // Tipo de documento desde el DTO
                dto.getNumeroDocumento(), // Número de documento desde el DTO
                dto.getNacionalidad()    // Nacionalidad desde el DTO
        );
    }

    // ── OBTENER TODOS ────────────────────────────────────────────────────────
    // SELECT * FROM clientes → convierte cada entidad a ResponseDTO
    public List<ClienteResponseDTO> obtenerTodos() {
        return clienteRepository.findAll() // Obtiene todos los clientes de la base de datos
                .stream()                  // Convierte la lista en un Stream para procesamiento funcional
                .map(this::mapToDTO)       // Aplica el mapeo a DTO en cada elemento del Stream
                .collect(Collectors.toList()); // Colecta el Stream como una nueva List
    }

    // ── OBTENER POR ID ───────────────────────────────────────────────────────
    // SELECT * FROM clientes WHERE id = ? → Optional para que el Controller maneje el 404
    public Optional<ClienteResponseDTO> obtenerPorId(Long id) {
        return clienteRepository.findById(id) // Busca el cliente por su id en MySQL
                .map(this::mapToDTO);          // Si existe, lo transforma a DTO; si no, Optional vacío
    }

    // ── GUARDAR ──────────────────────────────────────────────────────────────
    // INSERT INTO clientes → crea un nuevo cliente en la base de datos
    public ClienteResponseDTO guardar(ClienteRequestDTO dto) {
        Cliente cliente = mapToEntity(dto);          // Convierte el DTO validado a entidad JPA
        return mapToDTO(clienteRepository.save(cliente)); // Guarda en MySQL y convierte a DTO de respuesta
    }

    // ── ACTUALIZAR ───────────────────────────────────────────────────────────
    // UPDATE clientes WHERE id = ? → si existe el id, actualiza; si no, devuelve vacío
    public Optional<ClienteResponseDTO> actualizar(Long id, ClienteRequestDTO dto) {
        return clienteRepository.findById(id) // Busca el cliente existente por id
                .map(existente -> {           // Si existe, ejecuta la actualización
                    existente.setNombre(dto.getNombre());             // Actualiza el nombre
                    existente.setApellido(dto.getApellido());         // Actualiza el apellido
                    existente.setEmail(dto.getEmail());               // Actualiza el email
                    existente.setTelefono(dto.getTelefono());         // Actualiza el teléfono
                    existente.setTipoDocumento(dto.getTipoDocumento()); // Actualiza tipo documento
                    existente.setNumeroDocumento(dto.getNumeroDocumento()); // Actualiza número documento
                    existente.setNacionalidad(dto.getNacionalidad()); // Actualiza la nacionalidad
                    return mapToDTO(clienteRepository.save(existente)); // Guarda cambios y retorna DTO
                });
    }

    // ── ELIMINAR ─────────────────────────────────────────────────────────────
    // DELETE FROM clientes WHERE id = ?
    public void eliminar(Long id) {
        clienteRepository.deleteById(id); // Elimina el cliente con el id dado de la base de datos
    }

    // ── MÉTODOS DE BÚSQUEDA ──────────────────────────────────────────────────
    // Delegan al Repository y convierten resultados a DTOs

    // Busca clientes cuyo nombre contenga el texto (sin importar mayúsculas)
    public List<ClienteResponseDTO> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre) // Query Method del repositorio
                .stream().map(this::mapToDTO).collect(Collectors.toList()); // Mapea cada entidad a DTO
    }

    // Busca clientes cuyo apellido contenga el texto dado
    public List<ClienteResponseDTO> buscarPorApellido(String apellido) {
        return clienteRepository.findByApellidoContainingIgnoreCase(apellido) // Query Method del repositorio
                .stream().map(this::mapToDTO).collect(Collectors.toList()); // Mapea cada entidad a DTO
    }

    // Busca un cliente por su email (único en el sistema)
    public Optional<ClienteResponseDTO> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email) // Query Method exacto por email
                .map(this::mapToDTO); // Si existe, mapea a DTO
    }

    // Busca clientes por nacionalidad (sin distinción de mayúsculas)
    public List<ClienteResponseDTO> buscarPorNacionalidad(String nacionalidad) {
        return clienteRepository.findByNacionalidadIgnoreCase(nacionalidad) // Query Method del repositorio
                .stream().map(this::mapToDTO).collect(Collectors.toList()); // Mapea cada entidad a DTO
    }

    // Busca clientes por nombre completo (nombre + apellido) con SQL nativo
    public List<ClienteResponseDTO> buscarPorNombreCompleto(String nombreCompleto) {
        return clienteRepository.buscarPorNombreCompleto(nombreCompleto) // SQL nativo del repositorio
                .stream().map(this::mapToDTO).collect(Collectors.toList()); // Mapea resultados a DTOs
    }

}

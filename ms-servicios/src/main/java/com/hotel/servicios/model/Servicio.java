// Paquete de la capa de modelo del microservicio ms-servicios
package com.hotel.servicios.model;

// Importa las anotaciones JPA para el mapeo objeto-relacional
import jakarta.persistence.*;
import lombok.Data; // Genera métodos de acceso y utilidad con una anotación
import lombok.NoArgsConstructor; // Constructor vacío obligatorio para JPA
import lombok.AllArgsConstructor; // Constructor con todos los campos
import java.math.BigDecimal; // Tipo exacto para precios monetarios

// ═══════════════════════════════════════════════════════════
// Servicio.java — Entidad JPA del microservicio ms-servicios
//
// Representa el catálogo de servicios adicionales del hotel:
// spa, restaurante, lavandería, transporte, etc.
// ═══════════════════════════════════════════════════════════

@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor sin parámetros requerido por Hibernate
@AllArgsConstructor // Constructor completo para instanciar en el Service
@Entity // Define esta clase como entidad persistente JPA
@Table(name = "servicios") // Nombre de la tabla en db_hotel_servicios
public class Servicio {

    @Id // Columna PRIMARY KEY de la tabla servicios
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL genera el id con AUTO_INCREMENT
    private Long id; // Identificador único del servicio adicional

    // Nombre descriptivo del servicio (ej: "Masaje Relax", "Desayuno Buffet")
    @Column(nullable = false, length = 100) // Nombre del servicio obligatorio
    private String nombre;

    // Descripción detallada de qué incluye el servicio y sus condiciones
    @Column(length = 500) // Descripción opcional del contenido del servicio
    private String descripcion;

    // Precio unitario del servicio (por persona, por hora, o por uso según el caso)
    @Column(nullable = false, precision = 10, scale = 2) // Precio obligatorio con 2 decimales
    private BigDecimal precio;

    // Categoría del servicio (RESTAURANTE, SPA, LAVANDERIA, TRANSPORTE, ENTRETENIMIENTO, OTROS)
    @Column(nullable = false, length = 30) // Categoría obligatoria para filtrar el catálogo
    private String categoria;

    // Indica si el servicio está actualmente disponible para ser solicitado por huéspedes
    @Column(nullable = false) // Disponibilidad obligatoria para gestionar el catálogo activo
    private Boolean disponible;

}

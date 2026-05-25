// Paquete de la capa de modelo del microservicio ms-empleados
package com.hotel.empleados.model;

// Importa todas las anotaciones JPA para el mapeo con la base de datos MySQL
import jakarta.persistence.*;
import lombok.Data; // Genera getters, setters y métodos de Object automáticamente
import lombok.NoArgsConstructor; // Constructor vacío requerido por Hibernate/JPA
import lombok.AllArgsConstructor; // Constructor con todos los parámetros para el Service
import java.math.BigDecimal; // Tipo exacto para el salario sin pérdida de precisión
import java.time.LocalDate; // Tipo para la fecha de contratación sin componente de hora

// ═══════════════════════════════════════════════════════════
// Empleado.java — Entidad JPA del microservicio ms-empleados
//
// Representa al personal del hotel con su información
// laboral y de contacto.
// ═══════════════════════════════════════════════════════════

@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor sin argumentos obligatorio para JPA
@AllArgsConstructor // Constructor completo útil para crear objetos en la capa de servicio
@Entity // Marca esta clase como entidad persistente en la base de datos
@Table(name = "empleados") // Nombre de la tabla en db_hotel_empleados
public class Empleado {

    @Id // PRIMARY KEY de la tabla empleados
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL asigna el id con AUTO_INCREMENT
    private Long id; // Identificador único del empleado en el sistema

    // Nombre(s) del empleado para identificación personal
    @Column(nullable = false, length = 80) // Nombre obligatorio, máximo 80 caracteres
    private String nombre;

    // Apellido(s) del empleado
    @Column(nullable = false, length = 80) // Apellido obligatorio, máximo 80 caracteres
    private String apellido;

    // Cargo o puesto de trabajo del empleado (ej: Recepcionista, Ama de Llaves, Chef)
    @Column(nullable = false, length = 60) // Cargo obligatorio para asignación de tareas
    private String cargo;

    // Área del hotel a la que pertenece el empleado (RECEPCION, LIMPIEZA, COCINA, etc.)
    @Column(nullable = false, length = 30) // Departamento obligatorio para organización interna
    private String departamento;

    // Correo electrónico corporativo del empleado para comunicaciones internas
    @Column(nullable = false, unique = true, length = 120) // Email único obligatorio
    private String email;

    // Número de teléfono de contacto del empleado
    @Column(length = 20) // Teléfono opcional, máximo 20 caracteres
    private String telefono;

    // Fecha en que el empleado fue contratado por el hotel
    @Column(nullable = false) // Fecha de contratación obligatoria para cálculo de antigüedad
    private LocalDate fechaContratacion;

    // Salario mensual del empleado en moneda local (BigDecimal para precisión monetaria)
    @Column(nullable = false, precision = 10, scale = 2) // Salario obligatorio con 2 decimales
    private BigDecimal salario;

}

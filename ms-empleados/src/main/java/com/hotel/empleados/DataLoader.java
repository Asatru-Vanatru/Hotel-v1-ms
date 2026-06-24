package com.hotel.empleados;

import com.hotel.empleados.model.Empleado;
import com.hotel.empleados.repository.EmpleadoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public void run(String... args) {
        if (empleadoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] cargos = {"Recepcionista", "Ama de llaves", "Chef", "Conserje", "Gerente", "Mecánico"};
        String[] departamentos = {"RECEPCION", "LIMPIEZA", "COCINA", "MANTENIMIENTO", "ADMINISTRACION"};
        List<Empleado> empleados = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Empleado empleado = new Empleado();
            empleado.setNombre(faker.name().firstName());
            empleado.setApellido(faker.name().lastName());
            empleado.setCargo(cargos[faker.random().nextInt(cargos.length)]);
            empleado.setDepartamento(departamentos[faker.random().nextInt(departamentos.length)]);
            empleado.setEmail(faker.internet().emailAddress());
            empleado.setTelefono(faker.phoneNumber().phoneNumberInternational());
            empleado.setFechaContratacion(LocalDate.now().minusYears(faker.number().numberBetween(1, 10)));
            empleado.setSalario(BigDecimal.valueOf(faker.number().numberBetween(1800000, 5000000) / 100.0));
            empleados.add(empleado);
        }

        empleadoRepository.saveAll(empleados);
    }
}

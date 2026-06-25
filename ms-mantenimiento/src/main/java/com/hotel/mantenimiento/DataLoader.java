package com.hotel.mantenimiento;

import com.hotel.mantenimiento.model.Mantenimiento;
import com.hotel.mantenimiento.repository.MantenimientoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Override
    public void run(String... args) {
        if (mantenimientoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] estados = {"PENDIENTE", "EN_PROCESO", "COMPLETADO"};
        String[] prioridades = {"BAJA", "MEDIA", "ALTA", "CRITICA"};
        List<Mantenimiento> mantenimientos = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setHabitacionId((long) faker.number().numberBetween(1, 16));
            mantenimiento.setDescripcion("Revisión de " + faker.options().option("aire acondicionado", "fontanería", "electricidad", "cortinas"));
            mantenimiento.setEstado(estados[faker.random().nextInt(estados.length)]);
            mantenimiento.setPrioridad(prioridades[faker.random().nextInt(prioridades.length)]);
            mantenimiento.setEmpleadoId((long) faker.number().numberBetween(1, 16));
            mantenimiento.setFechaSolicitud(LocalDate.now().minusDays(faker.number().numberBetween(1, 20)));
            mantenimiento.setFechaCompletado(faker.bool().bool() ? LocalDate.now().minusDays(faker.number().numberBetween(1, 5)) : null);
            mantenimientos.add(mantenimiento);
        }

        mantenimientoRepository.saveAll(mantenimientos);
    }
}

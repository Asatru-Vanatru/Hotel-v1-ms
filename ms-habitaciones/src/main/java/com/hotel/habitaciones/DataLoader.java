package com.hotel.habitaciones;

import com.hotel.habitaciones.model.Habitacion;
import com.hotel.habitaciones.repository.HabitacionRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) {
        if (habitacionRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] tipos = {"SIMPLE", "DOBLE", "SUITE", "FAMILIAR"};
        String[] estados = {"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"};
        List<Habitacion> habitaciones = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Habitacion habitacion = new Habitacion();
            habitacion.setNumero(String.format("%03d", i + 101));
            habitacion.setTipo(tipos[faker.random().nextInt(tipos.length)]);
            habitacion.setCapacidad(faker.number().numberBetween(1, 5));
            habitacion.setPrecioNoche(BigDecimal.valueOf(faker.number().numberBetween(8000, 25000) / 100.0));
            habitacion.setDescripcion("Habitación " + faker.options().option("con vista al mar", "con balcón", "premium", "ejecutiva"));
            habitacion.setEstado(estados[faker.random().nextInt(estados.length)]);
            habitacion.setPiso(faker.number().numberBetween(1, 6));
            habitaciones.add(habitacion);
        }

        habitacionRepository.saveAll(habitaciones);
    }
}

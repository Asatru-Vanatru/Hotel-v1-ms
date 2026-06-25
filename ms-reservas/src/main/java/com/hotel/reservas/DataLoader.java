package com.hotel.reservas;

import com.hotel.reservas.model.Reserva;
import com.hotel.reservas.repository.ReservaRepository;
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
    private ReservaRepository reservaRepository;

    @Override
    public void run(String... args) {
        if (reservaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] estados = {"PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"};
        List<Reserva> reservas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Reserva reserva = new Reserva();
            reserva.setClienteId((long) faker.number().numberBetween(1, 21));
            reserva.setHabitacionId((long) faker.number().numberBetween(1, 16));
            reserva.setFechaIngreso(LocalDate.now().plusDays(faker.number().numberBetween(1, 15)));
            reserva.setFechaSalida(reserva.getFechaIngreso().plusDays(faker.number().numberBetween(1, 5)));
            reserva.setEstado(estados[faker.random().nextInt(estados.length)]);
            reserva.setTotalEstimado(BigDecimal.valueOf(faker.number().numberBetween(20000, 120000) / 100.0));
            reserva.setNumeroPasajeros(faker.number().numberBetween(1, 4));
            reservas.add(reserva);
        }

        reservaRepository.saveAll(reservas);
    }
}

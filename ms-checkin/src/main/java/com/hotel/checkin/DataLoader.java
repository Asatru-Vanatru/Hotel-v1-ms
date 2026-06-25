package com.hotel.checkin;

import com.hotel.checkin.model.CheckIn;
import com.hotel.checkin.repository.CheckInRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    private final CheckInRepository checkInRepository;

    public DataLoader(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    @Override
    public void run(String... args) {
        if (checkInRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        List<CheckIn> checkIns = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            CheckIn checkIn = new CheckIn();
            checkIn.setReservaId((long) faker.number().numberBetween(1, 31));
            checkIn.setClienteId((long) faker.number().numberBetween(1, 21));
            checkIn.setHabitacionId((long) faker.number().numberBetween(1, 16));
            checkIn.setFechaHoraCheckIn(LocalDateTime.now()
                    .minusDays(faker.number().numberBetween(1, 20))
                    .minusHours(faker.number().numberBetween(1, 12)));
            checkIn.setObservaciones(faker.options().option(
                    "Check-in normal",
                    "Llegada tarde",
                    "Huésped VIP",
                    "Recepción prioritaria",
                    "Solicita minibar"
            ));
            checkIns.add(checkIn);
        }

        checkInRepository.saveAll(checkIns);
    }
}

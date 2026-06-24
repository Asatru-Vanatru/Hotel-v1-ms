package com.hotel.checkout;

import com.hotel.checkout.model.CheckOut;
import com.hotel.checkout.repository.CheckOutRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CheckOutRepository checkOutRepository;

    @Override
    public void run(String... args) {
        if (checkOutRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        List<CheckOut> checkOuts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            CheckOut checkOut = new CheckOut();
            checkOut.setReservaId((long) faker.number().numberBetween(1, 31));
            checkOut.setClienteId((long) faker.number().numberBetween(1, 21));
            checkOut.setHabitacionId((long) faker.number().numberBetween(1, 16));
            checkOut.setFechaHoraCheckOut(LocalDateTime.now()
                    .minusDays(faker.number().numberBetween(1, 15))
                    .minusHours(faker.number().numberBetween(1, 8)));
            checkOut.setTotalFinal(BigDecimal.valueOf(faker.number().numberBetween(15000, 90000) / 100.0));
            checkOut.setDiasEstancia(faker.number().numberBetween(1, 5));
            checkOuts.add(checkOut);
        }

        checkOutRepository.saveAll(checkOuts);
    }
}

package com.hotel.consumos;

import com.hotel.consumos.model.Consumo;
import com.hotel.consumos.repository.ConsumoRepository;
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
    private ConsumoRepository consumoRepository;

    @Override
    public void run(String... args) {
        if (consumoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        List<Consumo> consumos = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Consumo consumo = new Consumo();
            consumo.setReservaId((long) faker.number().numberBetween(1, 21));
            consumo.setClienteId((long) faker.number().numberBetween(1, 21));
            consumo.setServicioId((long) faker.number().numberBetween(1, 21));
            consumo.setCantidad(faker.number().numberBetween(1, 4));
            consumo.setFechaConsumo(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 10)));
            consumo.setSubtotal(BigDecimal.valueOf(faker.number().numberBetween(5000, 40000) / 100.0));
            consumo.setObservaciones(faker.options().option(
                    "Consumido durante la estadía",
                    "Solicitud especial",
                    "Incluido en paquete",
                    "Requiere factura"
            ));
            consumos.add(consumo);
        }

        consumoRepository.saveAll(consumos);
    }
}

package com.hotel.pagos;

import com.hotel.pagos.model.Pago;
import com.hotel.pagos.repository.PagoRepository;
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
    private PagoRepository pagoRepository;

    @Override
    public void run(String... args) {
        if (pagoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] metodos = {"EFECTIVO", "TARJETA_CREDITO", "TARJETA_DEBITO", "TRANSFERENCIA"};
        String[] estados = {"COMPLETADO", "PENDIENTE", "CANCELADO"};
        List<Pago> pagos = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Pago pago = new Pago();
            pago.setReservaId((long) faker.number().numberBetween(1, 31));
            pago.setClienteId((long) faker.number().numberBetween(1, 21));
            pago.setMonto(BigDecimal.valueOf(faker.number().numberBetween(15000, 120000) / 100.0));
            pago.setMetodoPago(metodos[faker.random().nextInt(metodos.length)]);
            pago.setEstado(estados[faker.random().nextInt(estados.length)]);
            pago.setFechaPago(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 15)));
            pago.setReferencia("TX-" + faker.random().hex(6).toUpperCase());
            pagos.add(pago);
        }

        pagoRepository.saveAll(pagos);
    }
}

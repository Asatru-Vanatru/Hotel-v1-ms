package com.hotel.servicios;

import com.hotel.servicios.model.Servicio;
import com.hotel.servicios.repository.ServicioRepository;
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
    private ServicioRepository servicioRepository;

    @Override
    public void run(String... args) {
        if (servicioRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] categorias = {"RESTAURANTE", "SPA", "LAVANDERIA", "TRANSPORTE", "ENTRETENIMIENTO", "OTROS"};
        String[] nombresBase = {
                "Spa", "Restaurante", "Lavandería", "Traslado", "Piscina", "Fitness",
                "Business Center", "Room Service", "Bar", "Parking", "Concierge", "Tour",
                "Masaje", "Desayuno", "Catering"
        };

        List<Servicio> servicios = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Servicio servicio = new Servicio();
            String nombreBase = nombresBase[faker.random().nextInt(nombresBase.length)];
            String sufijo = faker.options().option("Premium", "Express", "VIP", "Deluxe", "24h", "Ejecutivo");
            servicio.setNombre(nombreBase + " " + sufijo);
            servicio.setDescripcion("Servicio de hotel generado automáticamente para " +
                    faker.options().option("huéspedes", "estancias", "viajes de negocios", "vacaciones"));
            servicio.setPrecio(BigDecimal.valueOf(faker.number().numberBetween(1000, 20000) / 100.0));
            servicio.setCategoria(categorias[faker.random().nextInt(categorias.length)]);
            servicio.setDisponible(true);
            servicios.add(servicio);
        }

        servicioRepository.saveAll(servicios);
    }
}

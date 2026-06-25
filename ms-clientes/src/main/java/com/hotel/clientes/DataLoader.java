package com.hotel.clientes;

import com.hotel.clientes.model.Cliente;
import com.hotel.clientes.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {
        if (clienteRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        List<Cliente> clientes = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Cliente cliente = new Cliente();
            cliente.setNombre(faker.name().firstName());
            cliente.setApellido(faker.name().lastName());
            cliente.setEmail(faker.internet().emailAddress());
            cliente.setTelefono(faker.phoneNumber().phoneNumberInternational());
            cliente.setTipoDocumento(faker.options().option("DNI", "PASAPORTE", "CEDULA"));
            cliente.setNumeroDocumento(faker.number().digits(8));
            cliente.setNacionalidad(faker.country().name());
            clientes.add(cliente);
        }

        clienteRepository.saveAll(clientes);
    }
}

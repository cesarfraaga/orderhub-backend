package com.core.orderhub.backend.repository;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldReturnTrueWhenCpfExists() {

        Client client = new Client();
        client.setCpf("12345678910");
        client.setName("César Fraga");
        client.setStatus(ClientStatus.ACTIVE);

        Client saved = clientRepository.save(client);
        clientRepository.flush();

        System.out.println("Id do cliente salvo: " +saved.getId());
        boolean exists = clientRepository.existsByCpf("12345678910");
        System.out.println("Exists: " + exists);

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenCpfDoesNotExist() {

        boolean exists = clientRepository.existsByCpf("00000000000");

        assertFalse(exists);
    }
}

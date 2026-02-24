package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.exception.ResourceConflictException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.ClientMapper;
import com.core.orderhub.backend.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClientSuccessfully() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName("César Fraga");
        clientDto.setCpf("12345678910");

        Client client = new Client();

        Client savedClient = new Client();
        savedClient.setId(1L);

        ClientDto responseDto = new ClientDto();
        responseDto.setId(1L);

        when(clientMapper.toEntity(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(savedClient);
        when(clientMapper.toDto(savedClient)).thenReturn(responseDto);

        ClientDto result = clientService.createClient(clientDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(clientRepository).save(client);
    }

    @Test
    void shouldThrowResourceConflictExceptionWhenCpfAlreadyExists() {

        ClientDto clientDto = new ClientDto();
        clientDto.setCpf("12345678910");

        when(clientRepository.existsByCpf(clientDto.getCpf()))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> clientService.createClient(clientDto)
        );

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        Long clientId = 1L;

        Client existingClient = new Client();
        existingClient.setId(clientId);

        ClientDto updateDto = new ClientDto();
        updateDto.setName("Novo nome");
        updateDto.setCpf("10987654321");

        Client savedClient = new Client();
        savedClient.setId(clientId);

        ClientDto outputDto = new ClientDto();
        outputDto.setId(clientId);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(existingClient));

        when(clientRepository.save(existingClient))
                .thenReturn(savedClient);

        when(clientMapper.toDto(savedClient))
                .thenReturn(outputDto);

        ClientDto result = clientService.updateClient(clientId, updateDto);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("Novo nome", existingClient.getName());
        assertEquals("10987654321", existingClient.getCpf());

        verify(clientRepository).save(existingClient);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateClientNotFound() {

        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        ClientDto clientDto = new ClientDto();

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.updateClient(clientId, clientDto)
        );

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowResourceConflictExceptionWhenCpfAlreadyExistsOnUpdateClient() {
        Long clientId = 1L;

        ClientDto clientDto = new ClientDto();
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        Client existsClient = new Client();
        existsClient.setId(clientId);
        existsClient.setName("Kaleb");
        existsClient.setCpf("12345678910");
        existsClient.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(existsClient));

        when(clientRepository.existsByCpfAndIdNot(clientDto.getCpf(), clientId))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> clientService.updateClient(clientId, clientDto)
        );

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldUpdateClientStatusSuccessfully() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        clientService.updateClientStatus(clientId, ClientStatus.INACTIVE);

        assertEquals(ClientStatus.INACTIVE, client.getStatus());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateClientStatusNotFound() {

        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.updateClientStatus(clientId, ClientStatus.INACTIVE)
        );
    }

    @Test
    void shouldFindClientByIdSuccessfully() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        ClientDto dto = new ClientDto();
        dto.setId(clientId);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(clientMapper.toDto(client))
                .thenReturn(dto);

        ClientDto result = clientService.findById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenFindClientByIdNotFound() {

        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.findById(clientId)
        );
    }

    @Test
    void shouldReturnClientListSuccessfully() {

        Client client = new Client();
        ClientDto dto = new ClientDto();

        when(clientRepository.findAll())
                .thenReturn(List.of(client));

        when(clientMapper.toDto(client))
                .thenReturn(dto);

        List<ClientDto> result = clientService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoClients() {

        when(clientRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<ClientDto> result = clientService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteClientSuccessfully() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        clientService.deleteById(clientId);

        verify(clientRepository).delete(client);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteClientNotFound() {

        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.deleteById(clientId)
        );

        verify(clientRepository, never()).delete(any());
    }
}

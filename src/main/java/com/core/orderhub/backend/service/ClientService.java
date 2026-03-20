package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.exception.ResourceConflictException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.ClientMapper;
import com.core.orderhub.backend.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    private static final String CLIENT_NOT_FOUND = "Client not found: ";

    public ClientDto createClient(ClientDto clientDto) {

        if (clientRepository.existsByCpf(clientDto.getCpf())) {
            throw new ResourceConflictException("CPF already registered");
        }

        Client client = clientMapper.toEntity(clientDto);

        client.changeStatus(ClientStatus.ACTIVE);

        Client savedClient = clientRepository.save(client);
        logger.info("Creating client... id={}", client.getId());
        return clientMapper.toDto(savedClient);
    }

    @Transactional
    public ClientDto updateClient(Long id, ClientDto clientDto) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CLIENT_NOT_FOUND + id));

        if (clientRepository.existsByCpfAndIdNot(clientDto.getCpf(), id)) {
            throw new ResourceConflictException("CPF already registered");
        }

        existingClient.update(clientDto);

        logger.info("Updating client... id={}", existingClient.getId());

        return clientMapper.toDto(existingClient);
    }

    @Transactional //ou tudo dá certo ou tudo é desfeito: consistência de dados
    public void updateClientStatus(Long id, ClientStatus newStatus) { //tenho que mudar pra não poder alterar para o status atual
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CLIENT_NOT_FOUND + id)
                );
        ClientStatus oldStatus = client.getStatus();
        client.changeStatus(newStatus);
        logger.info("Client {} status changed from {} to {}", id, oldStatus,newStatus);
    }

    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("client id not found: " + id)
                );
        return clientMapper.toDto(client);
    }

    public List<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(CLIENT_NOT_FOUND + id));
        clientRepository.delete(client);
    }
}
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

import java.util.ArrayList;
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

        client.setStatus(ClientStatus.ACTIVE);

        Client savedClient = clientRepository.save(client);
        logger.info("Creating client... id={}", client.getId());
        return clientMapper.toDto(savedClient);
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CLIENT_NOT_FOUND + id));

        if (clientRepository.existsByCpfAndIdNot(clientDto.getCpf(), id)) {
            throw new ResourceConflictException("CPF already registered");
        }

        //Precisei setar direto para que o hibernate não crie um novo objeto
        existingClient.setName(clientDto.getName());
        existingClient.setCpf(clientDto.getCpf());

        Client savedClient = clientRepository.save(existingClient);
        logger.info("Updating client... id={}", existingClient.getId());
        return clientMapper.toDto(savedClient);
    }

    @Transactional //ou tudo dá certo ou tudo é desfeito: consistência de dados
    public void updateClientStatus(Long id, ClientStatus newStatus) { //tenho que mudar pra não poder alterar para o status atual
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(CLIENT_NOT_FOUND + id)
                );
        ClientStatus oldStatus = client.getStatus();
        client.setStatus(newStatus);
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
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        for (Client client : clientList) {
            ClientDto clientDto = clientMapper.toDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    public void deleteById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(CLIENT_NOT_FOUND + id));
        clientRepository.delete(client);
    }

}

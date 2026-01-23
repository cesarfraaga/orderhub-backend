package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.ClientMapper;
import com.core.orderhub.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    public ClientDto createClient(ClientDto clientDto) {

        Client client = clientMapper.toEntity(clientDto);

        client.setStatus(ClientStatus.ACTIVE);

        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found: " + id));

        //Precisei setar direto para que o hibernate não crie um novo objeto
        existingClient.setName(clientDto.getName());
        existingClient.setCpf(clientDto.getCpf());

        Client savedClient = clientRepository.save(existingClient);

        return clientMapper.toDto(savedClient);
    }

    @Transactional //ou tudo dá certo ou tudo é desfeito: consistência de dados
    public void updateClientStatus(Long id, ClientStatus newStatus) { //need validations
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found: " + id)
                );
        client.setStatus(newStatus);
    }

    public ClientDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("client id null");
        }

        Client client = clientRepository.findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("client id not found: " + id)
                );
        return clientMapper.toDto(client);
    }

    public List<ClientDto> findAll() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        if (clientList.isEmpty()) throw new ResourceNotFoundException("clients not found");

        for (Client client : clientList) {
            ClientDto clientDto = clientMapper.toDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    public void deleteById(Long id) {
        if (id == null || !clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("client not found with id: " + id); //not found and exists are
        }
        clientRepository.deleteById(id);
    }

}

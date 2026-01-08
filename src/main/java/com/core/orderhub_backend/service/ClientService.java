package com.core.orderhub_backend.service;

import com.core.orderhub_backend.dto.ClientDto;
import com.core.orderhub_backend.entity.Client;
import com.core.orderhub_backend.mapper.ClientMapper;
import com.core.orderhub_backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientMapper clientMapper;

    public ClientDto createClient(ClientDto clientDto) {
/*        if (client.getName().isBlank() || client.getName().isEmpty()) {
            throw new
        }*/

        Client client = clientMapper.toEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    public ClientDto updateClient(ClientDto clientDto) {
        //if client not exist...
        Client client = clientMapper.toEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    public ClientDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("client id null");
        }

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("client id not found: " + id)); //change to resourcenotfoundexception - create class
        return clientMapper.toDto(client);
    }

    public List<ClientDto> findAll() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        if (clientList.isEmpty())
            throw new IllegalArgumentException("clients not found");

        for (Client client : clientList) {
            ClientDto clientDto = clientMapper.toDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    public void deleteById(Long id) {
        if (id == null || !clientRepository.existsById(id)) {
            throw new IllegalArgumentException("client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }


}

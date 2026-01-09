package com.core.orderhub_backend.service;

import com.core.orderhub_backend.dto.ClientDto;
import com.core.orderhub_backend.entity.Client;
import com.core.orderhub_backend.exception.ResourceNotFoundException;
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

        validateBeforeCreateOrUpdateClient(clientDto);

        Client client = clientMapper.toEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {

        if (clientDto.getId() == null) {
            throw new ResourceNotFoundException("Client not found with id " + clientDto.getId());
        }

        validateBeforeCreateOrUpdateClient(clientDto);

        Client existsClient = clientRepository.findById(clientDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Client not found: " + clientDto.getId()));

        Client client = clientMapper.toEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    public ClientDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("client id null");
        }

        Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("client id not found: " + id)); //change to resourcenotfoundexception - create class
        return clientMapper.toDto(client);
    }

    public List<ClientDto> findAll() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        if (clientList.isEmpty()) throw new IllegalArgumentException("clients not found");

        for (Client client : clientList) {
            ClientDto clientDto = clientMapper.toDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    public void deleteById(Long id) {
        if (id == null || !clientRepository.existsById(id)) {
            throw new IllegalArgumentException("client not found with id: " + id); //not found and exists are
        }
        clientRepository.deleteById(id);
    }

    private static void validateBeforeCreateOrUpdateClient(ClientDto clientDto) {

        validateName(clientDto.getName());

        validateCpf(clientDto.getCpf());
    }

    private static void validateCpf(String cpf) {
        int lengthCPF = 11;

        if (cpf.isBlank()) { //validate cpf with only numbers
            throw new NullPointerException("Client cpf cannot be null or empty");
        }

        if (cpf.length() != lengthCPF) {
            throw new IllegalArgumentException("The CPF must have 11 digits.");
        }
    }

    private static void validateName(String name) { //validate with only basic characters
        int minLengthName = 2;
        int maxLengthName = 50;

        if (name == null || name.isBlank()) {
            throw new NullPointerException("Client name cannot be null or empty");
        }

        if (name.length() < minLengthName || name.length() > maxLengthName) {
            throw new IllegalArgumentException("Client name cannot be less than 2 or more than 50 characters");
        }
    }
}

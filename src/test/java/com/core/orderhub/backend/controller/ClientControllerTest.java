package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.dto.ClientStatusDto;
import com.core.orderhub.backend.exception.ResourceConflictException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @Test
    void shouldReturnClientWhenIsSavedSuccessfully() throws Exception {

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.createClient(any(ClientDto.class)))
                .thenReturn(clientDto);

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("César"))
                .andExpect(jsonPath("$.cpf").value("12345678910"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsNullOnSaveClient() throws Exception {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(null);
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isBadRequest());
        verify(clientService, never()).createClient(any());
    }

    @Test
    void shouldReturnConflictWhenCpfAlreadyExistsOnSaveClient() throws Exception {

        ClientDto clientDto = new ClientDto();
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.createClient(any(ClientDto.class)))
                .thenThrow(new ResourceConflictException("CPF already registered"));

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnClientWhenIsUpdatedSuccessfully() throws Exception {

        Long clientId = 1L;

        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientId);
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.INACTIVE);

        when(clientService.updateClient(eq(clientId), any(ClientDto.class)))
                .thenReturn(clientDto);

        mockMvc.perform(put("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("César"))
                .andExpect(jsonPath("$.cpf").value("12345678910"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsNullOnUpdateClient() throws Exception {

        Long clientId = 1L;

        ClientDto clientDto = new ClientDto();
        clientDto.setName(null);
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        mockMvc.perform(put("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isBadRequest());
        verify(clientService, never()).updateClient(any(), any()); //aqui o erro acontece antes de chegar na service
    }

    @Test
    void shouldReturnConflictWhenCpfAlreadyExistsOnUpdateClient() throws Exception {

        Long clientId = 1L;

        ClientDto clientDto = new ClientDto();
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.updateClient(clientId, clientDto))
                .thenThrow(new ResourceConflictException("CPF already registered"));

        mockMvc.perform(put("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundWhenClientIdIsNotFoundOnUpdateClient() throws Exception {

        Long clientId = 1L;

        ClientDto clientDto = new ClientDto();
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.updateClient(clientId, clientDto))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(put("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNoContentWhenStatusIsUpdatedSuccessfully() throws Exception {

        Long clientId = 1L;

        ClientStatusDto dto = new ClientStatusDto();
        dto.setStatus(ClientStatus.ACTIVE);

        doNothing().when(clientService)
                .updateClientStatus(eq(clientId), eq(ClientStatus.ACTIVE));

        mockMvc.perform(patch("/client/{id}/status", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenClientDoesNotExistOnStatusUpdate() throws Exception {

        Long clientId = 1L;

        ClientStatusDto dto = new ClientStatusDto();
        dto.setStatus(ClientStatus.ACTIVE);

        doThrow(ResourceNotFoundException.class).when(clientService)
                .updateClientStatus(eq(clientId), eq(ClientStatus.ACTIVE));

        mockMvc.perform(patch("/client/{id}/status", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnClientWhenIdExists() throws Exception {

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.findById(1L)).thenReturn(clientDto);

        mockMvc.perform(get("/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("César"))
                .andExpect(jsonPath("$.cpf").value("12345678910"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundClientWhenIdNotExists() throws Exception {

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName("César");
        clientDto.setCpf("12345678910");
        clientDto.setStatus(ClientStatus.ACTIVE);

        when(clientService.findById(2L))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(get("/client/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllClients() throws Exception {

        ClientDto client1 = new ClientDto();
        client1.setId(1L);
        client1.setName("César");
        client1.setCpf("12345678910");
        client1.setStatus(ClientStatus.ACTIVE);

        ClientDto client2 = new ClientDto();
        client2.setId(2L);
        client2.setName("Kaleb");
        client2.setCpf("10987654321");
        client2.setStatus(ClientStatus.INACTIVE);

        when(clientService.findAll())
                .thenReturn(List.of(client1, client2));

        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("César"))
                .andExpect(jsonPath("$[1].status").value("INACTIVE"));

        verify(clientService).findAll();
    }

    @Test
    void shouldReturnNoContentWhenClientIsDeletedSuccessfully() throws Exception {

        Long clientId = 1L;

        doNothing().when(clientService).deleteById(clientId);

        mockMvc.perform(delete("/client/{id}", clientId))
                .andExpect(status().isNoContent());

        verify(clientService).deleteById(clientId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingClient() throws Exception {

        Long clientId = 1L;

        doThrow(new ResourceNotFoundException("Client not found"))
                .when(clientService).deleteById(clientId);

        mockMvc.perform(delete("/client/{id}", clientId))
                .andExpect(status().isNotFound());

        verify(clientService).deleteById(clientId);
    }
}
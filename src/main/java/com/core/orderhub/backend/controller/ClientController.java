package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.dto.ClientStatusDto;
import com.core.orderhub.backend.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> save(@RequestBody @Valid ClientDto clientDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ClientDto clientDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(id, clientDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ClientStatusDto dto
    ) {
        clientService.updateClientStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable @Positive Long id) {
        ClientDto clientDto = clientService.findById(id);
        return ResponseEntity.ok(clientDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll() {
        List<ClientDto> clientDtoList = clientService.findAll(); //Melhor alinhar com o que fiz na order, pra deixar mais enxuto
        return ResponseEntity.ok(clientDtoList);
    }
}
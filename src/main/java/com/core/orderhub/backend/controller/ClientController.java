package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.dto.ClientStatusDto;
import com.core.orderhub.backend.service.ClientService;
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

    @PostMapping("/save")
    public ResponseEntity<ClientDto> save(@RequestBody ClientDto clientDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(
            @PathVariable Long id, //+ legibilidade
            @RequestBody ClientDto clientDto
    ) { //Em REST, preciso identificar o recurso que vai ser att
        return ResponseEntity.status(HttpStatus.OK).body(clientService.updateClient(id, clientDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody ClientStatusDto dto
    ) {
        clientService.updateClientStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        ClientDto clientDto = clientService.findById(id);
        if (clientDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("findAll")
    public ResponseEntity<List<ClientDto>> findAll() {
        List<ClientDto> clientDtoList = clientService.findAll(); //Melhor alinhar com o que fiz na order, pra deixar mais enxuto
        return ResponseEntity.ok(clientDtoList);
    }
}
package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.dto.ClientDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    public Client(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
        this.status = ClientStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == ClientStatus.ACTIVE;
    }

    public void changeStatus(ClientStatus newStatus) {
        this.status = newStatus;
    }

    public void update(ClientDto dto) {
        this.name = dto.getName();
        this.cpf = dto.getCpf();
    }
}
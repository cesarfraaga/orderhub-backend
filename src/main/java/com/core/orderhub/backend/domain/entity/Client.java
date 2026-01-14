package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

}

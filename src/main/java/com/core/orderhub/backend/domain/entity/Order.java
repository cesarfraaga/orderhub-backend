package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "orders")  //OrderItem vai representar o produto dentro de um pedido específico.
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private BigDecimal total;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}

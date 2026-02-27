package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "orders")
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void addItem(Product product, Integer quantity) {
        if (this.status != OrderStatus.CREATED) {
            throw new BusinessException("Only orders with status CREATED can be modified");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setOrder(this);

        this.orderItemList.add(orderItem);

        this.recalculeTotal();
    }

    private void recalculeTotal() {
        this.total = orderItemList.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

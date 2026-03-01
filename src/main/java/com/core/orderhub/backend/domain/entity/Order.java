package com.core.orderhub.backend.domain.entity;

import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "orders")
@NoArgsConstructor
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

    public Order(Client client) {
        this.setClient(client);
        this.setStatus(OrderStatus.CREATED);
        this.setTotal(BigDecimal.ZERO);
        this.setCreatedAt(LocalDateTime.now());
    }

    public void addItem(Product product, Integer quantity) {
        validateOrderIsCreated();

        if (quantity == null || quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setOrder(this);

        product.decreaseStock(orderItem.getQuantity());

        this.orderItemList.add(orderItem);
        this.recalculateTotal();
    }

    public void removeItem(Product product) {

        validateOrderIsCreated();

        OrderItem itemToRemove = this.orderItemList.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException("Product not found in order")
                );

        product.increaseStock(itemToRemove.getQuantity());

        this.orderItemList.remove(itemToRemove);
        this.recalculateTotal();
    }

    public boolean isCreated() {
        return this.status == OrderStatus.CREATED;
    }

    public void changeStatus(OrderStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new BusinessException(
                    "Cannot change order status from " + this.status + " to " + newStatus
            );
        }
        this.status = newStatus;
    }

    private void validateOrderIsCreated() {
        if (!this.isCreated()) {
            throw new BusinessException("Only orders with status CREATED can be modified");
        }
    }

    private void recalculateTotal() {
        this.total = orderItemList.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

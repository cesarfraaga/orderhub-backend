package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.dto.AddOrderItemDto;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.dto.UpdateOrderStatusDto;
import com.core.orderhub.backend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/client/{clientId}")
    public ResponseEntity<OrderDto> save(@Positive @PathVariable Long clientId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(clientId));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDto> addOrderItem(
            @Positive @PathVariable Long id,
            @RequestBody @Valid AddOrderItemDto dto
    ) {
        return ResponseEntity.ok(orderService.addOrderItem(id, dto.getProductId(), dto.getQuantity()));
    }

    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<Void> removeOrderItem(
            @Positive @PathVariable Long id,
            @Positive @PathVariable Long productId
    ) {
        orderService.removeOrderItem(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @Positive @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusDto dto
    ) {
        orderService.updateOrderStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Positive @PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok((orderService.findAll()));
    }

}
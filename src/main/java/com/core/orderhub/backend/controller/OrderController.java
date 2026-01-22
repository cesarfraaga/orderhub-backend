package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.dto.AddOrderItemDto;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.dto.UpdateOrderStatusDto;
import com.core.orderhub.backend.service.OrderService;
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
    public ResponseEntity<OrderDto> save(@PathVariable Long clientId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(clientId));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDto> addOrderItem(
            @PathVariable Long id,
            @RequestBody AddOrderItemDto dto
    ) {
        return ResponseEntity.ok(orderService.addOrderItem(id, dto.getProductId(), dto.getQuantity()));
    }

    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<Void> removeOrderItem(
            @PathVariable Long id,
            @PathVariable Long productId
    ) {
        orderService.removeOrderItem(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusDto dto
    ) {
        orderService.updateOrderStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok((orderService.findAll()));
    }

}
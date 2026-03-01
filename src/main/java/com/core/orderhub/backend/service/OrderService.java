package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.exception.BusinessException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.OrderMapper;
import com.core.orderhub.backend.repository.ClientRepository;
import com.core.orderhub.backend.repository.OrderRepository;
import com.core.orderhub.backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final String ORDER_NOT_FOUND = "Order not found: ";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderDto createOrder(Long clientId) {

        Client client = findAndValidateClient(clientId);

        Order order = new Order(client);

        Order savedOrder = orderRepository.save(order);
        logger.info("Creating order... id={}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto addOrderItem(Long orderId, Long productId, Integer quantity) {

        Order order = findOrder(orderId);

        Product product = findProduct(productId);

        order.addItem(product, quantity);

        logger.info("Item added to order {} | product={} | qty={}",
                order.getId(), product.getId(), quantity
        );
        return orderMapper.toDto(order);
    }

    @Transactional
    public void removeOrderItem(Long orderId, Long productId) {

        Order order = findOrder(orderId);

        Product product = findProduct(productId);

        order.removeItem(product);

        logger.info("Item removed from order {} | product={}",
                order.getId(),
                product.getId()
        );
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order order = findOrder(orderId);

        OrderStatus currentStatus = order.getStatus();

        order.changeStatus(newStatus);

        logger.info("Updated order {} status from {} to {}",
                order.getId(), currentStatus, newStatus
        );
    }

    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ORDER_NOT_FOUND + id));
        return orderMapper.toDto(order);
    }

    public List<OrderDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException(ORDER_NOT_FOUND + id);
        }

        orderRepository.deleteById(id);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found: " + productId)
                );
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()
                        -> new ResourceNotFoundException(ORDER_NOT_FOUND + orderId)
                );
    }

    private Client findAndValidateClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found: " + clientId));

        if (!client.isActive()) {
            throw new BusinessException("Client is not Active");
        }
        return client;
    }
}
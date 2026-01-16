package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.domain.entity.OrderItem;
import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.OrderMapper;
import com.core.orderhub.backend.repository.ClientRepository;
import com.core.orderhub.backend.repository.OrderRepository;
import com.core.orderhub.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

/*   createOrder(clientId) OK
    addItem(orderId, productId, quantity)
    removeItem(orderId, itemId)
    updateStatus(orderId, newStatus)*/

    @Transactional
    public OrderDto createOrder(Long clientId) {

        Client client = findAndValidateClient(clientId);

        Order order = new Order();

        order.setClient(client);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);

    }

    @Transactional
    public OrderDto addOrderItem(Long orderId, Long productId, Integer quantity) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Order not found: " + orderId)
                );

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalArgumentException("Order is not active");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Product not found: " + productId)
                );

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException("Product is not active");  //business exception
        }

        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Product quantity not available. Available: " + product.getQuantity()); //business exception
        }

        OrderItem orderItem = new OrderItem();

        product.setQuantity(product.getQuantity() - quantity);

        orderItem.setUnitPrice(product.getPrice());
        BigDecimal subTotal = orderItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

        order.setTotal(order.getTotal().add(subTotal)); //Converti o quantity em bigdecimal

        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(subTotal);
        orderItem.setOrder(order);
        order.getOrderItemList().add(orderItem);

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    public OrderDto removeOrderItem(Long orderId, Long itemId) {
        return null;
    }

    public void updateOrderStatus() {

    }

    public OrderDto updateOrder(Long id, OrderDto orderDto) {

        Order existingOrder = orderRepository.findById(orderDto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + id));

/*        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);*/
        return null;
    }

    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order not found: " + id));
        return orderMapper.toDto(order);
    }

    public List<OrderDto> findAll() {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orderList) {
            OrderDto orderDto = orderMapper.toDto(order);
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    public void deleteById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order not found: " + id));
        orderRepository.deleteById(id);
    }

    private Client findAndValidateClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found: " + clientId));

        if (client.getStatus() != ClientStatus.ACTIVE) {
            throw new IllegalArgumentException("Client is not active"); //BusinessException
        }

        return client;
    }

}

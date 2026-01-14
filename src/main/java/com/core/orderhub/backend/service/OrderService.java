package com.core.orderhub.backend.service;

import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.OrderMapper;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public OrderDto createOrder(OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);

    }

    public OrderDto updateOrder(Long id, OrderDto orderDto) {

        Order existingOrder = orderRepository.findById(orderDto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + id));

        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
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

}

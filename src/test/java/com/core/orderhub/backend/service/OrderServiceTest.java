package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.exception.BusinessException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.OrderMapper;
import com.core.orderhub.backend.repository.OrderRepository;
import com.core.orderhub.backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    //addorderitem/removeorderitem {happy + error} / updatestatus

    @Test
    void shouldAddOrderItemSuccessfully() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.ACTIVE);
        product.setQuantity(10);
        product.setPrice(BigDecimal.valueOf(50));

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        OrderDto orderDto = mock(OrderDto.class);

        when(orderMapper.toDto(order))
                .thenReturn(orderDto);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));


        //Quando isso acontecer:
        OrderDto result = orderService.addOrderItem(orderId, productId, quantity);

        //Então
        assertNotNull(result);
        assertEquals(1, order.getOrderItemList().size());
        assertEquals(BigDecimal.valueOf(100), order.getTotal());
        assertEquals(8, product.getQuantity());

        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFound() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.addOrderItem(orderId, productId, quantity)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenOrderStatusIsNotCreated() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));


        assertThrows(
                BusinessException.class,
                () -> orderService.addOrderItem(orderId, productId, quantity)
        );

        verify(orderRepository, never()).save(any());

    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFound() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.addOrderItem(orderId, productId, quantity)
        );
        verify(orderRepository, never()).save(any());
    }

}

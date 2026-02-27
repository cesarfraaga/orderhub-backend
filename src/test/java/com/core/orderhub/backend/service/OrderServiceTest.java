package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.domain.entity.OrderItem;
import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.exception.BusinessException;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.OrderMapper;
import com.core.orderhub.backend.repository.ClientRepository;
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
class OrderServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setName("César");
        client.setCpf("123456");
        client.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDto orderDto = mock(OrderDto.class);

        when(orderMapper.toDto(any(Order.class)))
                .thenReturn(orderDto);

        OrderDto result = orderService.createOrder(clientId);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenClientIsNullOnCreateOrder() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setName("César");
        client.setCpf("12345678910");
        client.setStatus(ClientStatus.ACTIVE);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.createOrder(clientId)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenClientStatusIsNotActiveOnCreateOrder() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setName("César");
        client.setCpf("12345678910");
        client.setStatus(ClientStatus.INACTIVE);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        assertThrows(
                BusinessException.class,
                () -> orderService.createOrder(clientId)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldAddOrderItemSuccessfully() { //happy path

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
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundOnAddOrderItem() {

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
    void shouldThrowBusinessExceptionWhenOrderStatusIsNotCreatedOnAddOrderItem() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        Product product = new Product();
        product.setId(productId);
        product.setQuantity(10);
        product.setStatus(ProductStatus.ACTIVE);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

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

    @Test
    void shouldThrowBusinessExceptionWhenProductStatusIsNotActive() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.INACTIVE);
        product.setQuantity(10);
        product.setPrice(BigDecimal.valueOf(50));

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(
                BusinessException.class,
                () -> orderService.addOrderItem(orderId, productId, quantity)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenProductQuantityIsGreaterThanStock() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.ACTIVE);
        product.setQuantity(1);
        product.setPrice(BigDecimal.valueOf(50));

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        assertThrows(
                BusinessException.class,
                () -> orderService.addOrderItem(orderId, productId, quantity)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldRemoveOrderItemSuccessfully() {

        Long orderId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.ACTIVE);
        product.setQuantity(8);
        product.setPrice(BigDecimal.valueOf(50));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(BigDecimal.valueOf(50));
        orderItem.setSubtotal(BigDecimal.valueOf(100));

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setOrderItemList(new ArrayList<>());
        order.getOrderItemList().add(orderItem);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.removeOrderItem(orderId, productId);

        assertEquals(0, order.getOrderItemList().size());
        assertEquals(BigDecimal.ZERO, order.getTotal());
        assertEquals(10, product.getQuantity());

        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundOnRemoveOrderItem() {

        Long orderId = 1L;
        Long productId = 1L;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.removeOrderItem(orderId, productId)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenOrderStatusIsNotCreatedOnRemoveOrderItem() {

        Long orderId = 1L;
        Long productId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELED);
        order.setTotal(BigDecimal.ZERO);
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));


        assertThrows(
                BusinessException.class,
                () -> orderService.removeOrderItem(orderId, productId)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenItemNotFoundOnRemoveOrderItem() {

        Long orderId = 1L;
        Long productId = 99L;

        Product product = new Product();
        product.setId(1L);

        OrderItem existingItem = new OrderItem();
        existingItem.setProduct(product);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setOrderItemList(new ArrayList<>());
        order.getOrderItemList().add(existingItem);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                BusinessException.class,
                () -> orderService.removeOrderItem(orderId, productId)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldUpdateOrderStatusSuccessfully() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateOrderStatus() {

        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.PAID;

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.updateOrderStatus(orderId, newStatus)
        );
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenOrderStatusIsNotUpdatable() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.PAID;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setOrderItemList(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(
                BusinessException.class,
                () -> orderService.updateOrderStatus(orderId, newStatus)
        );
        verify(orderRepository, never()).save(any());
    }
}

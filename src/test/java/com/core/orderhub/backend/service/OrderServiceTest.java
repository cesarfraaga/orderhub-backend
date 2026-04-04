package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.domain.enums.OrderStatus;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.dto.OrderItemDto;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    private Client activeClient() {
        Client client = new Client("Kaleb", "01234567890");
        return client;
    }

    private ClientDto activeClientDto() {
        ClientDto clientDto = new ClientDto(1L, "Kaleb", "01234567890", ClientStatus.ACTIVE);
        return clientDto;
    }

    private Order createdOrder(Long id) {
        Order order = new Order(activeClient());
        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }

    private OrderDto createdOrderDto(Long id) {
        ClientDto clientDto = activeClientDto();
        OrderDto orderDto = new OrderDto(
                1L,
                clientDto.getId(),
                BigDecimal.valueOf(30.0),
                LocalDateTime.now(),
                List.of(
                        OrderItemDto.builder()
                                .productId(1L)
                                .quantity(10)
                                .productName("Item 1")
                                .unitPrice(BigDecimal.ONE)
                                .build()
                ),
                OrderStatus.CREATED);
        ReflectionTestUtils.setField(orderDto, "id", id);
        return orderDto;
    }

    private Product activeProduct(int quantity) {
        return new Product(1L, "Fone de ouvido", BigDecimal.valueOf(30.0),
                "fone sem fio",
                quantity,
                ProductStatus.ACTIVE);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        Long clientId = 1L;
        Client client = activeClient();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDto dto = mock(OrderDto.class);
        when(orderMapper.toDto(any(Order.class))).thenReturn(dto);

        OrderDto result = orderService.createOrder(clientId);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(clientRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(1L));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenClientInactive() {
        Client client = new Client();

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(1L));
    }

    @Test
    void shouldAddOrderItemSuccessfully() {
        Long orderId = 1L;
        Long productId = 1L;

        Order order = createdOrder(orderId);
        OrderDto orderDto = createdOrderDto(orderId);
        Product product = activeProduct(10);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        when(orderMapper.toDto(order))
                .thenReturn(orderDto);

        OrderDto result = orderService.addOrderItem(orderId, productId, 2);

        assertNotNull(result);
        assertEquals(1, order.getOrderItemList().size());
        assertEquals(BigDecimal.valueOf(60.0), order.getTotal());
        assertEquals(8, product.getQuantity());

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenOrderNotFoundOnAdd() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.addOrderItem(1L, 1L, 2));
    }

    @Test
    void shouldThrowWhenProductNotFoundOnAdd() {
        Order order = createdOrder(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.addOrderItem(1L, 1L, 2));
    }

    @Test
    void shouldThrowWhenStockInsufficient() {
        Order order = createdOrder(1L);
        Product product = activeProduct(1);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(BusinessException.class,
                () -> orderService.addOrderItem(1L, 1L, 2));
    }

    @Test
    void shouldRemoveOrderItemSuccessfully() {
        Long orderId = 1L;
        Long productId = 1L;

        Order order = createdOrder(orderId);
        Product product = activeProduct(10);

        order.addItem(product, 2);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        orderService.removeOrderItem(orderId, productId);

        assertEquals(0, order.getOrderItemList().size());
        assertEquals(BigDecimal.ZERO, order.getTotal());
        assertEquals(10, product.getQuantity());

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldUpdateStatusSuccessfully() {
        Order order = createdOrder(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.updateOrderStatus(1L, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void shouldThrowWhenInvalidStatusTransition() {
        Order order = createdOrder(1L);
        order.changeStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(BusinessException.class,
                () -> orderService.updateOrderStatus(1L, OrderStatus.PAID));
    }
}
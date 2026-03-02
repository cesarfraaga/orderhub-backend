package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.domain.entity.Client;
import com.core.orderhub.backend.domain.entity.Order;
import com.core.orderhub.backend.dto.OrderDto;
import com.core.orderhub.backend.dto.OrderItemDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldReturnOrderSuccessfullyOnSave() throws Exception {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        Order order = new Order(client);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(clientId);

        when(orderService.createOrder(clientId))
                .thenReturn(orderDto);

        mockMvc.perform(post("/order/client/{clientId}", clientId))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenClientIdNotExistsOnSaveOrder() throws Exception {

        Long clientId = 1L;

        when(orderService.createOrder(clientId))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(post("/order/client/{clientId}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenClientIdIsNotValidOnSaveOrder() throws Exception {

        Long clientId = -1L;

        mockMvc.perform(post("/order/client/{clientId}", clientId))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any());
    }

    @Test
    void shouldReturnOrderSuccessfullyOnAddOrderItem() throws Exception {

        Long orderId = 1L;
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(5);

        Order order = new Order(client);

        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);

        when(orderService.addOrderItem(eq(orderId), any(), any()))
                .thenReturn(orderDto);

        mockMvc.perform(post("/order/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemDto)))
                .andExpect(status().isOk());

        verify(orderService).addOrderItem(eq(orderId), any(), any());
    }

}

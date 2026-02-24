package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.domain.enums.ClientStatus;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.ClientDto;
import com.core.orderhub.backend.dto.ProductDto;
import com.core.orderhub.backend.dto.ProductStatusDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturnProductWhenIsSavedSuccessfully() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Headset Gamer");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);

        when(productService.createProduct(any(ProductDto.class)))
                .thenReturn(productDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(500.0))
                .andExpect(jsonPath("$.name").value("Headset Gamer"))
                .andExpect(jsonPath("$.description").value("Headset de alta qualidade"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsNullOnSaveProduct() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName(null);
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).createProduct(any());
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsNegativeOnSaveProduct() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Headset Gamer");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(-5);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).createProduct(any());
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsNullOnSaveProduct() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Headset Gamer");
        productDto.setPrice(null);
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).createProduct(any());
    }

    @Test
    void shouldReturnProductWhenIsUpdatedSuccessfully() throws Exception {

        Long productId = 1L;

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setName("Headset Gamer");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);

        when(productService.updateProduct(eq(productId), any(ProductDto.class)))
                .thenReturn(productDto);

        mockMvc.perform(put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(500.0))
                .andExpect(jsonPath("$.name").value("Headset Gamer"))
                .andExpect(jsonPath("$.description").value("Headset de alta qualidade"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsNullOnUpdateProduct() throws Exception {

        Long productId = 1L;

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setName("");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);

        mockMvc.perform(put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).updateProduct(any(), any()); //aqui o erro acontece antes de chegar na service
    }

    @Test
    void shouldReturnNotFoundWhenProductIdIsNotFoundOnUpdateProduct() throws Exception {

        Long productId = 1L;

        ProductDto productDto = new ProductDto();
        productDto.setName("Headset Gamer");
        productDto.setDescription("Headset de alta qualidade");

        when(productService.updateProduct(productId, productDto))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/client/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNoContentWhenStatusIsUpdatedSuccessfully() throws Exception {

        Long productId = 1L;

        ProductStatusDto dto = new ProductStatusDto();
        dto.setStatus(ProductStatus.ACTIVE);

        doNothing().when(productService)
                .updateProductStatus(eq(productId), eq(ProductStatus.ACTIVE));

        mockMvc.perform(patch("/product/{id}/status", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExistOnStatusUpdate() throws Exception {

        Long productId = 1L;

        ProductStatusDto dto = new ProductStatusDto();
        dto.setStatus(ProductStatus.ACTIVE);

        doThrow(ResourceNotFoundException.class).when(productService)
                .updateProductStatus(eq(productId), eq(ProductStatus.ACTIVE));

        mockMvc.perform(patch("/product/{id}/status", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnProductWhenIdExists() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Headset Gamer");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);
        productDto.setStatus(ProductStatus.ACTIVE);

        when(productService.findById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Headset Gamer"))
                .andExpect(jsonPath("$.price").value(500))
                .andExpect(jsonPath("$.description").value("Headset de alta qualidade"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundProductWhenIdNotExists() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Headset Gamer");
        productDto.setPrice(BigDecimal.valueOf(500.0));
        productDto.setDescription("Headset de alta qualidade");
        productDto.setQuantity(5);
        productDto.setStatus(ProductStatus.ACTIVE);

        when(productService.findById(2L))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/product/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllProducts() throws Exception {

        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Headset Gamer");
        product1.setPrice(BigDecimal.valueOf(500.0));
        product1.setDescription("Headset de alta qualidade");
        product1.setQuantity(5);
        product1.setStatus(ProductStatus.ACTIVE);

        ProductDto product2 = new ProductDto();
        product2.setId(1L);
        product2.setName("Teclado Gamer");
        product2.setPrice(BigDecimal.valueOf(350.0));
        product2.setDescription("Teclado de alta qualidade");
        product2.setQuantity(5);
        product2.setStatus(ProductStatus.INACTIVE);

        when(productService.findAll())
                .thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Headset Gamer"))
                .andExpect(jsonPath("$[1].status").value("INACTIVE"));

        verify(productService).findAll();
    }

    @Test
    void shouldReturnNoContentWhenProductIsDeletedSuccessfully() throws Exception {

        Long productId = 1L;

        doNothing().when(productService).deleteById(productId);

        mockMvc.perform(delete("/product/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService).deleteById(productId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProduct() throws Exception {

        Long productId = 1L;

        doThrow(new ResourceNotFoundException("Product not found"))
                .when(productService).deleteById(productId);

        mockMvc.perform(delete("/product/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService).deleteById(productId);
    }
}

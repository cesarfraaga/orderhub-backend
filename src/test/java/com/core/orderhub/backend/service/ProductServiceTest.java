package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.ProductDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.ProductMapper;
import com.core.orderhub.backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Test
    void shouldCreateProductSuccessfully() {

        ProductDto productDto = new ProductDto();
        productDto.setName("Óculos de sol");
        productDto.setPrice(BigDecimal.valueOf(150));
        productDto.setDescription("Óculos verão");
        productDto.setQuantity(5);

        Product product = new Product();

        Product savedProduct = new Product();
        savedProduct.setId(1L);

        ProductDto outputDto = new ProductDto();
        outputDto.setId(1L);

        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(outputDto);

        ProductDto result = productService.createProduct(productDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(productRepository).save(product);
    }
    @Test
    void shouldUpdateProductSuccessfully() {

        Long productId = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Novo nome");
        updateDto.setPrice(BigDecimal.valueOf(200));
        updateDto.setDescription("Nova descrição");

        Product savedProduct = new Product();
        savedProduct.setId(productId);

        ProductDto outputDto = new ProductDto();
        outputDto.setId(productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(savedProduct);

        when(productMapper.toDto(savedProduct))
                .thenReturn(outputDto);

        ProductDto result = productService.updateProduct(productId, updateDto);

        assertNotNull(result);
        assertEquals(productId, result.getId());

        verify(productRepository).save(existingProduct);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateProductNotFound() {

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(productId, new ProductDto())
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProductStatusSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.ACTIVE);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.updateProductStatus(productId, ProductStatus.INACTIVE);

        assertEquals(ProductStatus.INACTIVE, product.getStatus());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateProductStatusNotFound() {

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProductStatus(productId, ProductStatus.INACTIVE)
        );
    }

    @Test
    void shouldFindProductByIdSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productMapper.toDto(product))
                .thenReturn(productDto);

        ProductDto result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenFindByIdNotFound() {

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(productId)
        );
    }

    @Test
    void shouldReturnProductListSuccessfully() {

        Product product = new Product();
        ProductDto dto = new ProductDto();

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        when(productMapper.toDto(product))
                .thenReturn(dto);

        List<ProductDto> result = productService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {

        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<ProductDto> result = productService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.deleteById(productId);

        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteProductNotFound() {

        Long productId = 1L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteById(productId)
        );

        verify(productRepository, never()).delete(any());
    }
}

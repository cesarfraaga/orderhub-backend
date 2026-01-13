package com.core.orderhub_backend.service;

import com.core.orderhub_backend.dto.ProductDto;
import com.core.orderhub_backend.entity.Product;
import com.core.orderhub_backend.exception.ResourceNotFoundException;
import com.core.orderhub_backend.mapper.ProductMapper;
import com.core.orderhub_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService { //need: name/price/description validations

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto) {

        validateBeforeCreateOrUpdate(productDto);

        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + id));

        validateBeforeCreateOrUpdate(productDto);

        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product not found: " + id));
        return productMapper.toDto(product);
    }

    public List<ProductDto> findAll() {
        List<Product> productList = productRepository.findAll(); //Usar stream
        List<ProductDto> productDtoList = new ArrayList<>();
        //Lista vazia não é erro
        for (Product product : productList) {
            ProductDto productDto = productMapper.toDto(product);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product not found: " + id));
        productRepository.delete(product);
    }

    private static void validateBeforeCreateOrUpdate(ProductDto productDto) {

        validateProductName(productDto);

        validateProductPrice(productDto);

        validateProductDescription(productDto);
    }

    private static void validateProductPrice(ProductDto productDto) {
        if (productDto.getPrice() == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        BigDecimal maxPrice = new BigDecimal("100000");
        if (productDto.getPrice().compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Price must be less than or equal to 100000");
        }
    }

    private static void validateProductDescription(ProductDto productDto) {

        final int MIN_LENGTH_DESCRIPTION = 3;
        final int MAX_LENGTH_DESCRIPTION = 100;

        if (productDto.getDescription() == null || productDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }

        if (productDto.getDescription().length() < MIN_LENGTH_DESCRIPTION || productDto.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            throw new IllegalArgumentException("Product description cannot be less than 3 or more than 100 characters");
        }

        if (!productDto.getDescription().matches("^[a-zA-ZÀ-ÿ\\\\s]+$")) { //basic characters
            throw new IllegalArgumentException("The product description cannot contain special characters.");
        }
    }

    private static void validateProductName(ProductDto productDto) {

        final int MIN_LENGTH_NAME = 3;
        final int MAX_LENGTH_NAME = 50;

        if (productDto.getName() == null || productDto.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (productDto.getName().length() < MIN_LENGTH_NAME || productDto.getName().length() > MAX_LENGTH_NAME) {
            throw new IllegalArgumentException("Product name cannot be less than 3 or more than 50 characters");
        }

        if (!productDto.getName().matches("^[a-zA-ZÀ-ÿ\\\\s]+$")) { //basic characters
            throw new IllegalArgumentException("The product name cannot contain special characters.");
        }
    }

}

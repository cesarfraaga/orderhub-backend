package com.core.orderhub_backend.service;

import com.core.orderhub_backend.dto.ProductDto;
import com.core.orderhub_backend.entity.Product;
import com.core.orderhub_backend.exception.ResourceNotFoundException;
import com.core.orderhub_backend.mapper.ProductMapper;
import com.core.orderhub_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService { //need: name/price/description validations

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto) {

        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + id));

        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id null");
        }

        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product id not found: " + id));
        return productMapper.toDto(product);
    }

    public List<ProductDto> findAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = new ArrayList<>();

        if (productList.isEmpty()) throw new ResourceNotFoundException("Products not found");

        for (Product product : productList) {
            ProductDto productDto = productMapper.toDto(product);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public void deleteById(Long id) {
        if (id == null || !productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

}

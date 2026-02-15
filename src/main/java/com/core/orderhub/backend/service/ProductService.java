package com.core.orderhub.backend.service;

import com.core.orderhub.backend.domain.entity.Product;
import com.core.orderhub.backend.domain.enums.ProductStatus;
import com.core.orderhub.backend.dto.ProductDto;
import com.core.orderhub.backend.exception.ResourceNotFoundException;
import com.core.orderhub.backend.mapper.ProductMapper;
import com.core.orderhub.backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto) {


        Product product = productMapper.toEntity(productDto);

        product.setStatus(ProductStatus.ACTIVE);

        Product savedProduct = productRepository.save(product);
        logger.info("Creating product... id={}", product.getId());
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
        logger.info("Updating product... id={}", existingProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public void updateProductStatus(Long id, ProductStatus newStatus) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found: " + id)
                );
        ProductStatus oldStatus = product.getStatus();
        product.setStatus(newStatus);
        logger.info("Product {} status changed from {} to {}", id, oldStatus, newStatus);
    }

    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
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
}

package com.core.orderhub.backend.controller;

import com.core.orderhub.backend.dto.ProductDto;
import com.core.orderhub.backend.dto.ProductStatusDto;
import com.core.orderhub.backend.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ProductDto productDto
    ) { //Em REST, preciso identificar o recurso que vai ser att
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, productDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ProductStatusDto dto
    ) {
        productService.updateProductStatus(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable @Positive Long id) {
        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("products")
    public ResponseEntity<List<ProductDto>> findAll() {
        List<ProductDto> productDtoList = productService.findAll();
        return ResponseEntity.ok(productDtoList);
    }
}

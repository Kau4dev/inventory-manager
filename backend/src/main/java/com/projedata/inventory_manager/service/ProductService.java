package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.mapper.ProductMapper;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductCreatedDTO createProduct(ProductCreatedDTO productCreatedDTO) {
        Product product = productMapper.toEntity(productCreatedDTO);
        Product saved = productRepository.save(product);
        return productMapper.toCreatedDTO(saved);
    }

    public ProductViewDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toViewDTO(product);
    }

    public List<ProductViewDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toViewDTO)
                .toList();

    }

    public ProductUpdateDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productUpdateDTO.name());
        existingProduct.setPrice(productUpdateDTO.price());

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toUpdateDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }



}

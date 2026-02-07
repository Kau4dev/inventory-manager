package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductProductionSuggestionDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.exception.ResourceNotFoundException;
import com.projedata.inventory_manager.mapper.ProductMapper;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import com.projedata.inventory_manager.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductCreatedDTO createProduct(ProductCreatedDTO productCreatedDTO) {
        Product product = productMapper.toEntity(productCreatedDTO);
        Product saved = productRepository.save(product);
        return productMapper.toCreatedDTO(saved);
    }

    public ProductViewDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toViewDTO(product);
    }

    public List<ProductViewDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toViewDTO)
                .toList();

    }

    @Transactional
    public ProductUpdateDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        productMapper.updateFromDto(productUpdateDTO, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toUpdateDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        productRepository.delete(product);
    }

    public List<ProductViewDTO> getProducibleProducts() {
        List<Product> products = productRepository.findProducibleProducts();
        return products.stream()
                .map(productMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<ProductProductionSuggestionDTO> getProductionSuggestions() {
        List<Product> products = productRepository.findAllWithMaterials();
        List<ProductProductionSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            int maxQuantity = Integer.MAX_VALUE;
            for (ProductMaterial pm : product.getMaterials()) {
                int possible = pm.getMaterial().getStockQuantity() / pm.getRequiredQuantity();
                if (possible < maxQuantity) {
                    maxQuantity = possible;
                }
            }
            if (maxQuantity > 0) {
                BigDecimal productTotalValue = product.getPrice().multiply(BigDecimal.valueOf(maxQuantity));
                suggestions.add(new ProductProductionSuggestionDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        maxQuantity,
                        productTotalValue
                ));
                totalValue = totalValue.add(productTotalValue);
            }
        }

        suggestions.sort((a, b) -> b.price().compareTo(a.price()));
        return suggestions;
    }


}

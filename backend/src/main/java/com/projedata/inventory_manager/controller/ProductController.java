package com.projedata.inventory_manager.controller;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductProductionSuggestionDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.service.ProductMaterialService;
import com.projedata.inventory_manager.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMaterialService productMaterialService;

    @PostMapping
    public ResponseEntity<ProductCreatedDTO> createProduct(@RequestBody @Valid ProductCreatedDTO request) {
        ProductCreatedDTO response = productService.createProduct(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductViewDTO> getProductById(@PathVariable Long id) {
        ProductViewDTO response = productService.getProductById(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getAllProducts() {
        List<ProductViewDTO> responses = productService.getAllProducts();
        return ResponseEntity.status(200).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUpdateDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateDTO request) {
        ProductUpdateDTO response = productService.updateProduct(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }


    @PostMapping("/{productId}/materials")
    public ResponseEntity<Void> addMaterialToProduct(@PathVariable Long productId, @RequestBody @Valid ProductMaterialDTO request) {
        productMaterialService.addMaterialToProduct(productId, request);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{productId}/materials/{materialId}")
    public ResponseEntity<Void> removeMaterialFromProduct(@PathVariable Long productId, @PathVariable Long materialId) {
        productMaterialService.removeMaterialFromProduct(productId, materialId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/producible")
    public ResponseEntity<List<ProductViewDTO>> getProducibleProducts() {
        List<ProductViewDTO> responses = productService.getProducibleProducts();
        return ResponseEntity.status(200).body(responses);
    }

    @GetMapping("/production-suggestions")
    public ResponseEntity<Map<String, Object>> getProductionSuggestions() {
        List<ProductProductionSuggestionDTO> suggestions = productService.getProductionSuggestions();
        BigDecimal totalValue = suggestions.stream()
                .map(ProductProductionSuggestionDTO::totalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> response = Map.of(
                "suggestions", suggestions,
                "totalValue", totalValue
        );
        return ResponseEntity.ok(response);
    }
}


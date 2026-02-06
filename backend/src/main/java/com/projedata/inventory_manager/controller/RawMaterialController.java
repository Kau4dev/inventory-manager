package com.projedata.inventory_manager.controller;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rawMaterials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping
    public ResponseEntity<ProductCreatedDTO> createRawMaterial(@RequestBody @Valid ProductCreatedDTO request) {
        ProductCreatedDTO response = rawMaterialService.createProduct(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")

    public ResponseEntity<ProductViewDTO> getProductById(@PathVariable Long id) {
        ProductViewDTO response = rawMaterialService.getProductById(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductViewDTO>> getAllProducts() {
        List<ProductViewDTO> responses = rawMaterialService.getAllProducts();
        return ResponseEntity.status(200).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUpdateDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateDTO request) {
        ProductUpdateDTO response = rawMaterialService.updateProduct(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        rawMaterialService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }

}

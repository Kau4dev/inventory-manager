package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.mapper.RawProductMapper;
import com.projedata.inventory_manager.repository.RawProductRepository;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawProductService {

    private final RawProductRepository rawProductRepository;
    private final RawProductMapper rawProductMapper;

    @Transactional
    public ProductCreatedDTO createProduct(ProductCreatedDTO rawProductCreatedDTO) {
        RawMaterial rawMaterial = rawProductMapper.toEntity(rawProductCreatedDTO);
        RawMaterial saved = rawProductRepository.save(rawMaterial);
        return rawProductMapper.toCreatedDTO(saved);
    }

    public ProductViewDTO getProductById(Long id) {
        RawMaterial rawMaterial = rawProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        return rawProductMapper.toViewDTO(rawMaterial);
    }

    public List<ProductViewDTO> getAllProducts() {
        List<RawMaterial> rawMaterials = rawProductRepository.findAll();
        return rawMaterials.stream()
                .map(rawProductMapper::toViewDTO)
                .toList();

    }

    @Transactional
    public ProductUpdateDTO updateProduct(Long id, ProductUpdateDTO rawProductUpdateDTO) {
        RawMaterial existingRawMaterial = rawProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));

        rawProductMapper.updateFromDto(rawProductUpdateDTO, existingRawMaterial);

        RawMaterial updatedRawMaterial = rawProductRepository.save(existingRawMaterial);
        return rawProductMapper.toUpdateDTO(updatedRawMaterial);
    }

    @Transactional
    public void deleteProduct(Long id) {
        RawMaterial rawMaterial = rawProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        rawProductRepository.delete(rawMaterial);
    }

}

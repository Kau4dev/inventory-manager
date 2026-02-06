package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.mapper.RawMaterialMapper;
import com.projedata.inventory_manager.repository.RawMaterialRepository;
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
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;

    @Transactional
    public ProductCreatedDTO createProduct(ProductCreatedDTO rawMaterialCreatedDTO) {
        RawMaterial rawMaterial = rawMaterialMapper.toEntity(rawMaterialCreatedDTO);
        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toCreatedDTO(saved);
    }

    public ProductViewDTO getProductById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        return rawMaterialMapper.toViewDTO(rawMaterial);
    }

    public List<ProductViewDTO> getAllProducts() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        return rawMaterials.stream()
                .map(rawMaterialMapper::toViewDTO)
                .toList();

    }

    @Transactional
    public ProductUpdateDTO updateProduct(Long id, ProductUpdateDTO rawMaterialUpdateDTO) {
        RawMaterial existingRawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));

        rawMaterialMapper.updateFromDto(rawMaterialUpdateDTO, existingRawMaterial);

        RawMaterial updatedRawMaterial = rawMaterialRepository.save(existingRawMaterial);
        return rawMaterialMapper.toUpdateDTO(updatedRawMaterial);
    }

    @Transactional
    public void deleteProduct(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        rawMaterialRepository.delete(rawMaterial);
    }

}

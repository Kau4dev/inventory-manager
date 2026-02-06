package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.repository.ProductRepository;
import com.projedata.inventory_manager.repository.ProductMaterialRepository;
import com.projedata.inventory_manager.repository.RawMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMaterialService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductMaterialRepository productMaterialRepository;

    @Transactional
    public void addMaterialToProduct(Long productId, ProductMaterialDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        RawMaterial material = rawMaterialRepository.findById(dto.materialId())
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));

        ProductMaterial association = new ProductMaterial();
        association.setProduct(product);
        association.setMaterial(material);
        association.setRequiredQuantity(dto.requiredQuantity());

        productMaterialRepository.save(association);
    }

    @Transactional
    public void removeMaterialFromProduct(Long productId, Long materialId) {
        ProductMaterial association = productMaterialRepository.findByProduct_IdAndMaterial_Id(productId, materialId)
                .orElseThrow(() -> new EntityNotFoundException("Association not found"));

        productMaterialRepository.delete(association);
    }
}

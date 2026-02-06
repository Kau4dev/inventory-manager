package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.mapper.ProductMaterialMapper;
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
    private final ProductMaterialMapper productMaterialMapper;

    @Transactional
    public void addMaterialToProduct(Long productId, ProductMaterialDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        RawMaterial material = rawMaterialRepository.findById(dto.materialId())
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));

        ProductMaterial association = productMaterialMapper.toEntity(dto);
        association.setProduct(product);
        association.setMaterial(material);

        product.getMaterials().add(association);

        productMaterialRepository.save(association);
        productRepository.save(product);
    }

    @Transactional
    public void removeMaterialFromProduct(Long productId, Long materialId) {
        ProductMaterial association = productMaterialRepository.findByProduct_IdAndMaterial_Id(productId, materialId)
                .orElseThrow(() -> new EntityNotFoundException("Association not found"));

        association.getProduct().getMaterials().remove(association);

        productMaterialRepository.delete(association);
        productRepository.save(association.getProduct());
    }
}

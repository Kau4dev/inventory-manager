package com.projedata.inventory_manager.repository;

import com.projedata.inventory_manager.model.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {

    Optional<ProductMaterial> findByProduct_IdAndMaterial_Id(Long productId, Long materialId);
}

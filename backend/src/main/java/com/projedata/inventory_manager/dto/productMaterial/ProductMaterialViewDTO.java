package com.projedata.inventory_manager.dto.productMaterial;

public record ProductMaterialViewDTO(
        Long id,
        Long materialId,
        String materialName,
        int requiredQuantity,
        int stockQuantity) {
}

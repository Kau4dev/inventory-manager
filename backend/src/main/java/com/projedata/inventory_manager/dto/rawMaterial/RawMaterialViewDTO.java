package com.projedata.inventory_manager.dto.rawMaterial;

public record RawMaterialViewDTO(
        Long id,
        String name,
        Integer stockQuantity
) {
}

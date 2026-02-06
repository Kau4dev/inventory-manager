package com.projedata.inventory_manager.dto.rawProduct;

public record RawProductViewDTO(
        Long id,
        String name,
        Integer stockQuantity
) {
}

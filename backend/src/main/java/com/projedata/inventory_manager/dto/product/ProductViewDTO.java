package com.projedata.inventory_manager.dto.product;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductViewDTO(
        Long id,
        String name,
        BigDecimal price,
        List<ProductMaterialDTO> materials) {
}

package com.projedata.inventory_manager.dto.product;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialViewDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductViewDTO(
        Long id,
        String name,
        BigDecimal price,
        List<ProductMaterialViewDTO> materials,
        boolean producible) {
}

package com.projedata.inventory_manager.dto.product;

import java.math.BigDecimal;

public record ProductViewDTO(
        Long id,
        String name,
        BigDecimal price) {
}

package com.projedata.inventory_manager.dto.product;

import java.math.BigDecimal;

public record ProductProductionSuggestionDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer maxQuantity,
        BigDecimal totalValue) {
}

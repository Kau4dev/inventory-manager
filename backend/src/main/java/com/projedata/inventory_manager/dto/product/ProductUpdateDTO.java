package com.projedata.inventory_manager.dto.product;


import java.math.BigDecimal;

public record ProductUpdateDTO(
        String name,
        BigDecimal price
) {
}

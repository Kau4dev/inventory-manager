package com.projedata.inventory_manager.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateDTO(
        @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
        String name,

        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price
) {
}

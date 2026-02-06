package com.projedata.inventory_manager.dto.productMaterial;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductMaterialDTO(

        @NotNull
        Long materialId,

        @NotNull(message = "Required quantity cannot be null")
        @Positive(message = "Required quantity must be positive")
        BigDecimal requiredQuantity
) {
}

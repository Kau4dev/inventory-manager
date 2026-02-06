package com.projedata.inventory_manager.dto.productMaterial;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record ProductMaterialDTO(

        @NotNull
        Long materialId,

        @NotNull(message = "Required quantity cannot be null")
        @Positive(message = "Required quantity must be positive")
        Integer requiredQuantity
) {
}

package com.projedata.inventory_manager.dto.rawMaterial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record RawMaterialCreatedDTO(

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "StockQuantity cannot be null")
        @Positive(message = "StockQuantity must be positive")
        Integer stockQuantity
) {
}

package com.projedata.inventory_manager.dto.rawMaterial;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record RawMaterialUpdateDTO(
        @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
        String name,

        @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
        Integer stockQuantity
) {
}

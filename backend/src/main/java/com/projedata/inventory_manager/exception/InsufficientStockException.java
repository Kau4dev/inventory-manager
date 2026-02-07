package com.projedata.inventory_manager.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String materialName, int required, int available) {
        super("Insufficient stock for material '%s': required %d, available %d"
                .formatted(materialName, required, available));
    }
}

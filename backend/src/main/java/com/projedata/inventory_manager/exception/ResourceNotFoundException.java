package com.projedata.inventory_manager.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super("%s with id %d not found".formatted(resourceName, id));
    }
}

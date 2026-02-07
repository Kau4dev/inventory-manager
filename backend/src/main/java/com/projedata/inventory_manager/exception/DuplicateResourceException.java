package com.projedata.inventory_manager.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String field, String value) {
        super("%s with %s '%s' already exists".formatted(resourceName, field, value));
    }
}

package com.app.service;

/**
 * Thrown when a registration/creation would collide with an existing, unique value
 * (username, email). Kept distinct from IllegalArgumentException so
 * GlobalExceptionHandler can return 409 Conflict per the API spec, instead of the
 * generic 400 Bad Request that IllegalArgumentException maps to.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}

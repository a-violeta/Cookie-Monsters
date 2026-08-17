package com.app.service;

/**
 * Thrown when a registration includes a dateOfBirth that puts the user under
 * the minimum allowed age. Kept distinct from IllegalArgumentException so
 * GlobalExceptionHandler can return a clear, specific error code.
 */
public class UserTooYoungException extends RuntimeException {
    public UserTooYoungException(int minAge) {
        super("The minimum age to register is " + minAge);
    }
}

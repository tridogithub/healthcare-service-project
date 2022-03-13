package com.trido.healthcare.exception;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(String message) {
        super(message);
    }
}

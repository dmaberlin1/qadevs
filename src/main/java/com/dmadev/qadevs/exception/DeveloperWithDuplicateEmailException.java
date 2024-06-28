package com.dmadev.qadevs.exception;

public class DeveloperWithDuplicateEmailException extends RuntimeException{
    public DeveloperWithDuplicateEmailException(String message) {
        super(message);
    }
}

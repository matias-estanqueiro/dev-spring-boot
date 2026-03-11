package com.matiasae.springboot.cruddemo.exception;

public class EmployeeBadRequestException extends RuntimeException {
    public EmployeeBadRequestException(String message) {
        super(message);
    }

    public EmployeeBadRequestException(Throwable cause) {
        super(cause);
    }

    public EmployeeBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

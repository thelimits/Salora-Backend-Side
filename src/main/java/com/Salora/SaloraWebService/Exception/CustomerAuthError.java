package com.Salora.SaloraWebService.Exception;

public class CustomerAuthError extends RuntimeException{
    public CustomerAuthError(String message) {
        super(message);
    }
}

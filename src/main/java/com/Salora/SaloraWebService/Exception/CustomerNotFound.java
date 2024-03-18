package com.Salora.SaloraWebService.Exception;

public class CustomerNotFound extends RuntimeException{
    public CustomerNotFound(String message) {
        super(message);
    }
}

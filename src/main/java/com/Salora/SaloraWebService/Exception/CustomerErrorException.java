package com.Salora.SaloraWebService.Exception;

public class CustomerErrorException extends RuntimeException{
    public CustomerErrorException(String message) {
        super(message);
    }
}

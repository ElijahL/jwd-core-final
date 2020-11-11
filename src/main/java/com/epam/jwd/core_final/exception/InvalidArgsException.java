package com.epam.jwd.core_final.exception;

public class InvalidArgsException extends RuntimeException{
    public InvalidArgsException(String errorMessage){
        super(errorMessage);
    }
}

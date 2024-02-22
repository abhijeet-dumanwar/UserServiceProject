package com.scaler.Exceptions;

public class TokenNotExistsOrAlreadyExpiredException extends Exception{
    public TokenNotExistsOrAlreadyExpiredException(String message){
        super(message);
    }
}

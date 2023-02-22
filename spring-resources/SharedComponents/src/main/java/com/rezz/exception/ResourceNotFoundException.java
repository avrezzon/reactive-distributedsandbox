package com.rezz.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private Object payload;

    public ResourceNotFoundException(String message, Object payload){
        super(message);
        this.payload = payload;
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}

package com.iit.lab2;

public class EntityNotFoundException extends Exception{
    private String type;

    public EntityNotFoundException(String type, String message){
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

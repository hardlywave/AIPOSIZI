package com.iit.ad.respnose;

public class EntityException extends Exception{
    String type;

    public String getType() {
        return type;
    }

    public EntityException(String message, String type) {
        super(message);
        this.type = type;
    }
}

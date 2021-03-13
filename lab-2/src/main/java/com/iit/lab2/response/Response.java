package com.iit.lab2.response;

public class Response {
    private final int status;
    private final String message;
    private Object data;

    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}

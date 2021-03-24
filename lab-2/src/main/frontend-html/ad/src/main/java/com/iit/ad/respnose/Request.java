package com.iit.ad.respnose;

public class Request {
    private int status;
    private String message;
    private String type;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Request() {
    }

    public Request(int status, String message, String type, Object data) {
        this.status = status;
        this.message = message;
        this.type = type;
        this.data = data;
    }
}

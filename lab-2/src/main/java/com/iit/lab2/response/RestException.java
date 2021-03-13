package com.iit.lab2.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class RestException extends Exception{
    private HttpStatus status;

    public RestException(HttpStatus status, String message){
        super(message);
        this.status = status;
        log.warn(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

package com.distribution;

import lombok.Getter;

@Getter
public enum HttpCode {

    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    NOT_ALLOWED(405, "Not Allowed");

    private final Integer code;
    private final String description;

    HttpCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

}

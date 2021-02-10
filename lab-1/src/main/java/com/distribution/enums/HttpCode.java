package com.distribution.enums;

import java.nio.file.Path;

public enum HttpCode {

    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    NOT_IMPLEMENTED(501, "Not implemented");

    private final Integer code;
    private final String description;

    HttpCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
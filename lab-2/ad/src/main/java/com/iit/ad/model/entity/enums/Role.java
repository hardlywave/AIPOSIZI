package com.iit.ad.model.entity.enums;


public enum Role {
    ANON("ANON"),
    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

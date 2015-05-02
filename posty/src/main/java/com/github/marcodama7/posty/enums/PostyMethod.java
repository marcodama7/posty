package com.github.marcodama7.posty.enums;

/**
 * Accepted Http methods
 */
public enum PostyMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    PATCH("PATCH");

    private String rawMethod;
    PostyMethod(String rawMethod) {
        this.rawMethod = rawMethod;
    }

    public String getRawMethod() {
        return rawMethod;
    }

}

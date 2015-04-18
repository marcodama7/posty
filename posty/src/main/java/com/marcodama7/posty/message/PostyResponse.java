package com.marcodama7.posty.message;

import java.util.List;
import java.util.Map;

/**
 * wrapper class for encapsulate original request, and server response. Output of PTAsync
 */
public class PostyResponse {

    String response;
    Throwable error;
    String errorMessage;
    Integer responseCode;
    Map<String, List<String>> headers;
    protected PostyRequest originalRequest;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public PostyRequest getOriginalRequest() {
        return originalRequest;
    }

    public boolean inError() {
        return error != null || errorMessage != null;
    }

    public void setOriginalRequest(PostyRequest originalRequest) {
        this.originalRequest = originalRequest;
    }
}

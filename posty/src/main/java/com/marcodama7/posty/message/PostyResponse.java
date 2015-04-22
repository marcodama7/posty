package com.marcodama7.posty.message;

import org.json.JSONException;
import org.json.JSONObject;

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
    Map<String, List<String>> headers;  // response headers
    protected PostyRequest originalRequest; // original PostyRequest

    PostyResponse previousResponse; // if there are mutiple http calls, this is the previous response


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

    /**
     *  If server response is Json data, return JsonObject created from this data, otherwise return null
     * @return
     */
    public JSONObject getJsonResponse() {
        JSONObject jsonObject = null;
        if (getResponse() != null && getResponse().length() > 0) {
            try {
                jsonObject = new JSONObject(getResponse());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
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

    /**
     * If there is a multiple http calls, return a previous response
     * @return
     */
    public PostyResponse getPreviousResponse() {
        return previousResponse;
    }

    public void setPreviousResponse(PostyResponse previousResponse) {
        this.previousResponse = previousResponse;
    }
}

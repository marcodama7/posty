package com.marcodama7.posty.message;
import com.marcodama7.posty.core.PostyMethod;
import com.marcodama7.posty.listeners.PostyResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic Http request.
 */

public class PostyRequest {

    String method; // method: GET | POST | DELETE | PUT etc ...
    String uri;
    Map<String, String> headers; // if headers != null and are size 0, no headers are sended !
    PostyBody body;    // body of the request
    int timeoutMillisecond = 10000;

    PostyResponseListener postyResponseListener; // callback when the http request is finished!


    public PostyRequest(){
        method = PostyMethod.GET;
        headers = null;
        body = new PostyBody();
        timeoutMillisecond = 10000;
    }

    public PostyRequest(String uri) {
        this();
        this.uri = uri;
    }

    public int getTimeoutMillisecond() {

        return timeoutMillisecond;
    }

    public void setTimeoutMillisecond(int timeoutMillisecond) {
        this.timeoutMillisecond = timeoutMillisecond;
    }

    public String getMethod() {
        if (method == null) {
            method = PostyMethod.GET;
        }
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(name, value);
    }

    public PostyResponseListener getPostyResponseListener() {
        return postyResponseListener;
    }

    public void setPostyResponseListener(PostyResponseListener postyResponseListener) {
        this.postyResponseListener = postyResponseListener;
    }

    public PostyBody getBody() {
        if (body == null) {
            body = new PostyBody();

        }
        return body;
    }

    public void setBody(PostyBody body) {
        this.body = body;
    }

    /* True if this http method has not a response */
    public boolean hasNoResponse() {
        return getMethod() == PostyMethod.HEAD;
    }
}

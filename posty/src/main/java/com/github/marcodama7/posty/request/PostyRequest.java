package com.github.marcodama7.posty.request;
import com.github.marcodama7.posty.enums.PostyMethod;
import com.github.marcodama7.posty.listeners.PostyErrorListener;
import com.github.marcodama7.posty.listeners.PostyResponseListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic Http request.
 */

public class PostyRequest {

    PostyMethod method; // method: GET | POST | DELETE | PUT etc ...
    String uri;
    Map<String, String> headers; // if headers != null and are size 0, no headers are sended !
    PostyBody body;    // body of the request
    int timeoutMillisecond = 10000;
    Object tag; // used for retrieve particularly request in case of multiple calls

    PostyResponseListener postyResponseListener; // callback when the http request is sended and received (with successfull and, if postyErrorListener is null, with error)!
    PostyErrorListener postyErrorListener; // callback when the http request is sended and received with errors

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

    public PostyMethod getMethod() {
        if (method == null) {
            method = PostyMethod.GET;
        }
        return method;
    }

    public void setMethod(PostyMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public URI getBaseURI() {
        try {
            return new URI(getBaseURIAsString());
        } catch (Exception e) {
            return null;
        }
    }

    public String getBaseURIAsString() {
        if (getUri() == null || getUri().length() <1) {
            return null;
        }
        try {
            URI uri = new URI(getUri());
            return uri.getScheme() + "://"+uri.getHost();
        }
        catch (Exception ex) {
            String uri = (getUri().contains("://") && getUri().indexOf("://") + 3 < getUri().length())? getUri().substring(0,getUri().indexOf("://") + 3) : getUri();
            if (uri.contains("/") && uri.split("\\/").length>0) {
                uri = uri.split("\\/")[0];
            }
            return uri;
        }
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

    public PostyErrorListener getPostyErrorListener() {
        return postyErrorListener;
    }

    public void setPostyResponseListener(PostyResponseListener postyResponseListener) {
        this.postyResponseListener = postyResponseListener;
    }

    public void setPostyErrorListener(PostyErrorListener postyErrorListener) {
        this.postyErrorListener = postyErrorListener;
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

    /**
     * Retrieve tag for request, for identify more easyly a particular requests in case of multiple requests
     * @return
     */
    public Object getTag() {
        return tag;
    }

    /**
     * Add tag to this request, for retrieve more easyly in case of multiple calls
     * @param tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }
}

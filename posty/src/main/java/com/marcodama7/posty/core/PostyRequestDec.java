package com.marcodama7.posty.core;

import com.marcodama7.posty.listeners.PostyResponseListener;
import com.marcodama7.posty.message.PostyAttachment;
import com.marcodama7.posty.message.PostyBody;
import com.marcodama7.posty.message.PostyRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Decorator for create PostyRequest more easily
 */
public class PostyRequestDec {

    PostyRequest request;

    private PostyRequestDec(){

    }

    public PostyRequestDec(PostyRequest request){
        this.request = request;
    }

    public PostyRequestDec timeout(Integer timeoutMillisecond) {
        request.setTimeoutMillisecond(timeoutMillisecond);
        return this;
    }

    public PostyRequestDec onResponse(PostyResponseListener postyResponseListener) {
        request.setPostyResponseListener(postyResponseListener);
        return this;
    }

    public PostyRequestDec headers(Map<String, String> headers) {
        request.setHeaders(headers);
        return this;
    }

    public PostyRequestDec header(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    public PostyRequestDec method(String method) {
        request.setMethod(method);
        return this;
    }

    public static PostyAsyncTask call(
            String uri,
            Map<String,String> headers,
            PostyBody body,
            String method,
            PostyResponseListener postyResponseListener) {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        PostyRequest request = new PostyRequest();
        request.setUri(uri);
        request.setHeaders(headers);
        request.setBody(body);
        request.setMethod(method);
        request.setPostyResponseListener(postyResponseListener);
        postyAsyncTask.execute(request);
        return postyAsyncTask;
    }

    public static PostyAsyncTask call(
            String uri,
            Map<String,String> headers,
            PostyBody body,
            PostyResponseListener postyResponseListener) {
        return call(uri, headers, body, PostyMethod.GET, postyResponseListener);
    }

    public static PostyAsyncTask call(PostyRequest[] requests) {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(requests);
        return postyAsyncTask;
    }

    public static PostyAsyncTask call(PostyRequest request) {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(request);
        return postyAsyncTask;
    }

    public PostyAsyncTask call() {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(request);
        return postyAsyncTask;
    }



    /* Overloading body methods */
    public PostyRequestDec body(PostyBody postyBody) {
        request.setBody(postyBody);
        return this;
    }

    public PostyRequestDec body(Map<String, String> paramethers) {
        request.setBody(new PostyBody(paramethers,false));
        return this;
    }

    public PostyRequestDec body(Map<String, String> paramethers, boolean postDataUrlEncoded) {
        request.setBody(new PostyBody(paramethers, postDataUrlEncoded));
        return this;
    }

    public PostyRequestDec body(String customRawBody) {
        request.setBody(new PostyBody(customRawBody));
        return this;
    }

    public PostyRequestDec body(JSONObject jsonObject) {
        request.setBody(new PostyBody(jsonObject));
        return this;
    }

    public PostyRequestDec body(JSONArray jsonArray) {
        request.setBody(new PostyBody(jsonArray));
        return this;
    }

    public PostyRequestDec body(List<PostyAttachment> files) {
        request.setBody(new PostyBody(files));
        return this;
    }

    public PostyRequestDec body(Map<String, String> paramethers, List<PostyAttachment> files) {
        request.setBody(new PostyBody(paramethers, files));
        return this;
    }


}

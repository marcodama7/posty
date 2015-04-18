package com.marcodama7.posty.core;

import com.marcodama7.posty.listeners.PostyResponseListener;
import com.marcodama7.posty.message.PostyFile;
import com.marcodama7.posty.message.PostyBody;
import com.marcodama7.posty.message.PostyRequest;
import com.marcodama7.posty.message.PostyResponse;

import org.json.JSONArray;
import org.json.JSONObject;

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


    /***********************************************************************************************************************************/
    /* Body creation methods
    /***********************************************************************************************************************************/


    /**
     * Creation of body passing custom PostyBody object
     * @param postyBody
     * @return PostyRequestDec
     */
    public PostyRequestDec body(PostyBody postyBody) {
        request.setBody(postyBody);
        return this;
    }

    /**
     * Creation of body passing paramethers of pairs of key-value (in a Map<String, String> )
     * @param paramethers
     * @return PostyRequestDec
     */
    public PostyRequestDec body(Map<String, String> paramethers) {
        if (request.getBody() != null && request.getBody().hasBody()) {
            request.getBody().addBody(paramethers);
        }
        else {
            request.setBody(new PostyBody(paramethers,false));
        }
        return this;
    }

    /**
     * Adding in a body one paramether
     * @param key
     * @param value
     * @return
     */
    public PostyRequestDec body(String key, String value) {
        if (request.getBody() == null) {
            request.setBody(new PostyBody());
        }
        request.getBody().addBodyParam(key, value);
        return this;
    }


    /**
     * Creation of body passing paramethers and store in body urlencoded,
     * (ex key1=val1&key2=val2....)
     * @param paramethers
     * @return
     */
    public PostyRequestDec bodyUrlEncoded(Map<String, String> paramethers) {
        if (request.getBody() != null && request.getBody().hasBody()) {
            request.getBody().addBodyParamUrlEncoded(paramethers);
        }
        else {
            request.setBody(new PostyBody(paramethers,true));
        }
        return this;
    }

    /**
     * Creation of body with custom content (Raw values stored in a string)
     * @param customRawBody
     * @return
     */
    public PostyRequestDec body(String customRawBody) {
        request.setBody(new PostyBody(customRawBody));
        return this;
    }

    /**
     * Creation of body with JsonObject
     * @param jsonObject
     * @return
     */
    public PostyRequestDec body(JSONObject jsonObject) {
        request.setBody(new PostyBody(jsonObject));
        return this;
    }

    /**
     * Creation of body with jsonArray
     * @param jsonArray
     * @return
     */
    public PostyRequestDec body(JSONArray jsonArray) {
        request.setBody(new PostyBody(jsonArray));
        return this;
    }

    /**
     * Creation of body with a list of files (to be uploaded!)
     * Any file is stored in a object PostyFile
     * @param files
     * @return
     */
    public PostyRequestDec body(List<PostyFile> files) {
        request.setBody(new PostyBody(files));
        return this;
    }

    /**
     * Adding PostyFile
     * @param file
     * @return
     */
    public PostyRequestDec body(PostyFile file) {
        if (request.getBody() == null) {
            request.setBody(new PostyBody());
        }
        request.getBody().add(file);
        return this;
    }

    /**
     * Adding a file in a body
     * @param key
     * @param filePath
     * @param mimeType
     * @return
     */
    public PostyRequestDec file(String key, String filePath, String mimeType) {
        if (request.getBody() == null) {
            request.setBody(new PostyBody());
        }
        request.getBody().addFile(key, filePath, mimeType);
        return this;
    }

    /**
     * Adding a file in a body
     * @param key
     * @param filePath
     * @return
     */
    public PostyRequestDec file(String key, String filePath) {
        if (request.getBody() == null) {
            request.setBody(new PostyBody());
        }
        request.getBody().addFile(key, filePath);
        return this;
    }

    /**
     * Creation of body
     * @param paramethers
     * @param files
     * @return
     */
    public PostyRequestDec body(Map<String, String> paramethers, List<PostyFile> files) {
        request.setBody(new PostyBody(paramethers, files));
        return this;
    }


}

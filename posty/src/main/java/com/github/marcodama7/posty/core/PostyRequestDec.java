package com.github.marcodama7.posty.core;

import com.github.marcodama7.posty.listeners.PostyMultipleResponseListener;
import com.github.marcodama7.posty.listeners.PostyResponseListener;
import com.github.marcodama7.posty.message.PostyFile;
import com.github.marcodama7.posty.message.PostyBody;
import com.github.marcodama7.posty.message.PostyRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decorator for create PostyRequest more easily
 */
public class PostyRequestDec {

    ArrayList<PostyRequest> requests;


    private PostyRequestDec(){

    }

    private ArrayList<PostyRequest> getRequests(){
        if (requests == null) requests = new ArrayList<PostyRequest>();
        return requests;
    }

    private PostyRequest getLastRequest(){
        return (getRequests().size()>0) ? getRequests().get(getRequests().size()-1) : null;
    }

    public PostyRequestDec(PostyRequest request){
        getRequests().add(request);
    }

    public PostyRequestDec timeout(Integer timeoutMillisecond) {
        getLastRequest().setTimeoutMillisecond(timeoutMillisecond);
        return this;
    }

    /**
     * Adding another request
     * @param uri
     * @return
     */
    public PostyRequestDec newRequest(String uri){
        getRequests().add(new PostyRequest(uri));
        return this;
    }

    /**
     * Set call back when http call is finished (successfully or with errors)
     * @param postyResponseListener
     * @return
     */
    public PostyRequestDec onResponse(PostyResponseListener postyResponseListener) {
        getLastRequest().setPostyResponseListener(postyResponseListener);
        return this;
    }

    public PostyRequestDec headers(Map<String, String> headers) {
        getLastRequest().setHeaders(headers);
        return this;
    }

    public PostyRequestDec header(String name, String value) {
        getLastRequest().addHeader(name, value);
        return this;
    }

    public PostyRequestDec method(String method) {
        getLastRequest().setMethod(method);
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

    /**
     * Call a request created. If there are multiple requests, call last request.
     * @return
     */
    public PostyAsyncTask call() {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(getLastRequest());
        return postyAsyncTask;
    }

    /**
     * Call multiple requests. Passing a callback called when all requests are received
     * @param postyMultipleResponseListener
     * @return
     */
    public PostyAsyncTask multipleCall(PostyMultipleResponseListener postyMultipleResponseListener) {
        if (getRequests() == null || getRequests().size() < 1) {
            if (postyMultipleResponseListener != null) {
                postyMultipleResponseListener.onResponse(null,0);
            }
            return null;
        }
        else {
            PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
            postyAsyncTask.setPostyMultipleResponseListener(postyMultipleResponseListener);
            postyAsyncTask.execute(getRequests().toArray(new PostyRequest[getRequests().size()]));
            return postyAsyncTask;
        }

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
        getLastRequest().setBody(postyBody);
        return this;
    }

    /**
     * Creation of body passing paramethers of pairs of key-value (in a Map<String, String> )
     * @param paramethers
     * @return PostyRequestDec
     */
    public PostyRequestDec body(Map<String, String> paramethers) {
        if (getLastRequest().getBody() != null && getLastRequest().getBody().hasBody()) {
            getLastRequest().getBody().addBody(paramethers);
        }
        else {
            getLastRequest().setBody(new PostyBody(paramethers, false));
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
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().addBodyParam(key, value);
        return this;
    }


    /**
     * Creation of body passing paramethers and store in body urlencoded,
     * (ex key1=val1&key2=val2....)
     * @param paramethers
     * @return
     */
    public PostyRequestDec bodyUrlEncoded(Map<String, String> paramethers) {
        if (getLastRequest().getBody() != null && getLastRequest().getBody().hasBody()) {
            getLastRequest().getBody().addBodyParamUrlEncoded(paramethers);
        }
        else {
            getLastRequest().setBody(new PostyBody(paramethers, true));
        }
        return this;
    }

    /**
     * Creation of body with custom content (Raw values stored in a string)
     * @param customRawBody
     * @return
     */
    public PostyRequestDec body(String customRawBody) {
        getLastRequest().setBody(new PostyBody(customRawBody));
        return this;
    }

    /**
     * Creation of body with JsonObject
     * @param jsonObject
     * @return
     */
    public PostyRequestDec body(JSONObject jsonObject) {
        getLastRequest().setBody(new PostyBody(jsonObject));
        return this;
    }

    /**
     * Creation of body with jsonArray
     * @param jsonArray
     * @return
     */
    public PostyRequestDec body(JSONArray jsonArray) {
        getLastRequest().setBody(new PostyBody(jsonArray));
        return this;
    }

    /**
     * Creation of body with a list of files (to be uploaded!)
     * Any file is stored in a object PostyFile
     * @param files
     * @return
     */
    public PostyRequestDec body(List<PostyFile> files) {
        getLastRequest().setBody(new PostyBody(files));
        return this;
    }

    /**
     * Adding PostyFile
     * @param file
     * @return
     */
    public PostyRequestDec body(PostyFile file) {
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().add(file);
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
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().addFile(key, filePath, mimeType);
        return this;
    }

    /**
     * Adding a file in a body
     * @param key
     * @param filePath
     * @return
     */
    public PostyRequestDec file(String key, String filePath) {
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().addFile(key, filePath);
        return this;
    }

    /**
     * Creation of body
     * @param paramethers
     * @param files
     * @return
     */
    public PostyRequestDec body(Map<String, String> paramethers, List<PostyFile> files) {
        getLastRequest().setBody(new PostyBody(paramethers, files));
        return this;
    }




}

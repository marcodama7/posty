package com.github.marcodama7.posty.core;

import android.net.Uri;

import com.github.marcodama7.posty.listeners.PostyErrorListener;
import com.github.marcodama7.posty.listeners.PostyMultipleResponseListener;
import com.github.marcodama7.posty.listeners.PostyResponseListener;
import com.github.marcodama7.posty.request.PostyFile;
import com.github.marcodama7.posty.request.PostyBody;
import com.github.marcodama7.posty.enums.PostyMethod;
import com.github.marcodama7.posty.request.PostyRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Decorator for create PostyRequest more easily
 */
public class PostyRequestDec {

    List<PostyRequest> requests;

    private PostyRequestDec(){

    }

    /**
     * Constructor for create decorator PostyRequest from list of request (to call in sequences)
     * @param requests
     */
    public PostyRequestDec(List<PostyRequest> requests){
        this.requests = requests;
    }

    /**
     * Add tag to current request, for retrieve more easyly in case of multiple calls
     * @param tag
     * @return
     */
    public PostyRequestDec addTag(Object tag) {
        getLastRequest().setTag(tag);
        return this;
    }



    /**
     * Adding another request
     * @param uri to call
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec newRequest(String uri){
        getRequests().add(new PostyRequest(uri));
        return this;
    }

    public List<PostyRequest> getRequests(){
        if (requests == null) requests = new ArrayList<>();
        return requests;
    }

    public PostyRequest getLastRequest(){
        return (getRequests().size()>0) ? getRequests().get(getRequests().size()-1) : null;
    }

    public PostyRequestDec(PostyRequest request){
        getRequests().add(request);
    }

    /**
     * Set timeout of connection
     * @param timeoutMillisecond: timeout connection in milliseconds
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec timeout(Integer timeoutMillisecond) {
        getLastRequest().setTimeoutMillisecond(timeoutMillisecond);
        return this;
    }

    /**
     * Set headers of requests
     * @param headers: Map of pairs key-values of headers to add
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec headers(Map<String, String> headers) {
        getLastRequest().setHeaders(headers);
        return this;
    }

    /**
     * Add one header in a current headers of request
     * @param name: name of headers to add
     * @param value: value of headers to add
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec header(String name, String value) {
        getLastRequest().addHeader(name, value);
        return this;
    }

    /**
     * Set method
     * @param method Postymethod (POST, GET, PUT, ...)
     * @return instance of PostyRequestDec
     */
    public PostyRequestDec method(PostyMethod method) {
        getLastRequest().setMethod(method);
        return this;
    }

    /**
     * Set call back when http call is finished (successfully or, if onErrorListener is null also with errors)
     * @param postyResponseListener a method to call when the requests is sended and a response is received
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec onResponse(PostyResponseListener postyResponseListener) {
        getLastRequest().setPostyResponseListener(postyResponseListener);
        return this;
    }

    /**
     * Set call back when http call is finished with errors
     * @param postyErrorListener a method to call when error occurred
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec onError(PostyErrorListener postyErrorListener) {
        getLastRequest().setPostyErrorListener(postyErrorListener);
        return this;
    }

    public static PostyAsyncTask call(PostyRequest[] requests) {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(requests);
        return postyAsyncTask;
    }

    /**
     *  Call a request created. If there are multiple requests, call last request.
     *  Parameter is an handler called when request is sended and received.
     *  In case of error, the parameter of responseListener.onResponse(PostyResponse response)
     *  contain the error
     * @param responseListener callBack called when request is sended and received
     * @return instance of PostyRequestDec
     */
    public PostyAsyncTask call(PostyResponseListener responseListener) {
        if (getLastRequest() != null) {
            getLastRequest().setPostyResponseListener(responseListener);
        }
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(getLastRequest());
        return postyAsyncTask;
    }

    /**
     *  Call a request created. If there are multiple requests, call last request.
     * @return instance of PostyRequestDec
     */
    public PostyAsyncTask call() {
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(getLastRequest());
        return postyAsyncTask;
    }

    /**
     *  Call a request created. If there are multiple requests, call last request.
     *  The first parameter is an handler called when request is sended  with successfull,
     *  a second parameter is an handler called when request is sended with error.
     * @param responseListener: callback called when request is sended successfully
     * @param errorListener: callback called when an error occurred
     * @return instance of PostyRequestDec
     */
    public PostyAsyncTask call(PostyResponseListener responseListener, PostyErrorListener errorListener) {
        if (getLastRequest() != null) {
            getLastRequest().setPostyResponseListener(responseListener);
            getLastRequest().setPostyErrorListener(errorListener);
        }
        PostyAsyncTask postyAsyncTask = new PostyAsyncTask();
        postyAsyncTask.execute(getLastRequest());
        return postyAsyncTask;
    }

    /**
     * Call multiple requests. Passing a callback called when all requests are received
     * @param postyMultipleResponseListener a method wich is called when all requests are finished
     * @return AsynchTask wich manage the current http requests
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
     * @param postyBody a object wich encapsulate current body to send over http
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(PostyBody postyBody) {
        getLastRequest().setBody(postyBody);
        return this;
    }

    /**
     * Creation of body passing parameters of pairs of key-value (in a Map)
     * @param parameters map of key-values parameters
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(Map<String, String> parameters) {
        if (getLastRequest().getBody() != null && getLastRequest().getBody().hasBody()) {
            getLastRequest().getBody().addBody(parameters);
        }
        else {
            getLastRequest().setBody(new PostyBody(parameters, false));
        }
        return this;
    }

    /**
     * Adding in a body one parameter
     * @param key a key of parameter
     * @param value value of parameter
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(String key, String value) {
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().addBodyParam(key, value);
        return this;
    }

    /**
     * Adding in a URL query string parameter
     * @param queryStringKey a key of parameter
     * @param queryStringValue value of parameter
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec queryString(String queryStringKey, String queryStringValue) {
        if (getLastRequest().getUri() != null && getLastRequest().getUri().length() > 0
                && queryStringKey != null && queryStringValue != null) {
            String uri = getLastRequest().getUri();
            boolean otherParameters = false;
            if (!uri.contains("?")) {
                uri += "?";
            }
            else if (uri.contains("=") && uri.indexOf("?") < uri.lastIndexOf("=")) {
                otherParameters = true;
            }

            uri = (otherParameters) ? uri+"&"+ Uri.encode(queryStringKey) +"="+ Uri.encode(queryStringValue) :  uri+Uri.encode(queryStringKey) +"="+ Uri.encode(queryStringValue);
            getLastRequest().setUri(uri);
        }
        return this;
    }

    /**
     * Adding in a URL query string parameter
     * @param queryString a raw queryString
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec queryString(String queryString) {
        if (getLastRequest().getUri() != null && getLastRequest().getUri().length() > 0
                && queryString != null && queryString.length()>0) {
            String uri = getLastRequest().getUri();

            if (!uri.contains("?")) {
                uri += "?";
            }
            uri = (!uri.contains("?")) ? uri + "?" + queryString : uri + queryString;
            getLastRequest().setUri(uri);
        }
        return this;
    }

    /**
     * Adding in a URL query string parameters
     * @param queryStringParameters map of key - values of query string
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec queryString(Map<String, String> queryStringParameters) {
        if (getLastRequest().getUri() != null && getLastRequest().getUri().length() > 0
                && queryStringParameters != null && queryStringParameters.size() > 0) {
            String uri = getLastRequest().getUri();
            boolean otherParameters = false;
            if (!uri.contains("?")) {
                uri += "?";
            }
            else if (uri.contains("=") && uri.indexOf("?") < uri.lastIndexOf("=")) {
                otherParameters = true;
            }
            for (String key : queryStringParameters.keySet()) {
                uri = (otherParameters) ? uri+"&"+ Uri.encode(key) +"="+ Uri.encode(queryStringParameters.get(key)) :  uri+Uri.encode(key) +"="+ Uri.encode(queryStringParameters.get(key));
                otherParameters = true;
            }
            getLastRequest().setUri(uri);
        }
        return this;
    }


    /**
     * Creation of body passing parameters and store in body urlencoded,
     * @param parameters map of parameters to add in a current body of request
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec bodyUrlEncoded(Map<String, String> parameters) {
        if (getLastRequest().getBody() != null && getLastRequest().getBody().hasBody()) {
            getLastRequest().getBody().addBodyParamUrlEncoded(parameters);
        }
        else {
            getLastRequest().setBody(new PostyBody(parameters, true));
        }
        return this;
    }

    public PostyRequestDec bodyUrlEncoded(String parametherName, String parametherValue) {
        if (getLastRequest().getBody() == null || (!getLastRequest().getBody().hasBody())) {
            Map<String,String> paramethers = new HashMap<>();
            paramethers.put(parametherName, parametherValue);
            getLastRequest().setBody(new PostyBody(paramethers, true));
        }
        else {
            getLastRequest().getBody().addBodyParamUrlEncoded(parametherName, parametherValue);
        }
        return this;
    }

    /**
     * Creation of body with custom content (Raw values stored in a string)
     * @param customRawBody string that contains a custom body
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(String customRawBody) {
        getLastRequest().setBody(new PostyBody(customRawBody));
        return this;
    }

    /**
     * Creation of body with JsonObject
     * @param jsonObject to add in a current body
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(JSONObject jsonObject) {
        getLastRequest().setBody(new PostyBody(jsonObject));
        return this;
    }

    /**
     * Creation of body with jsonArray
     * @param jsonArray to add in a current body
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(JSONArray jsonArray) {
        getLastRequest().setBody(new PostyBody(jsonArray));
        return this;
    }

    /**
     * Creation of body with a list of files (to be uploaded!)
     * Any file is stored in a object PostyFile
     * @param files List of object PostyFile, that store the information of files to be uploaded
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(List<PostyFile> files) {
        getLastRequest().setBody(new PostyBody(files));
        return this;
    }

    /**
     * Adding PostyFile
     * @param file: object PostyFile, that store the information of files to be uploaded
     * @return instance of PostyRequestDec, wich store the current request(s)
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
     * @param key field key of file to be uploaded
     * @param filePath absolute filepath of file to upload
     * @param mimeType mimetyper of file
     * @return instance of PostyRequestDec, wich store the current request(s)
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
     * @param key field key of file to be uploaded
     * @param filePath absolute filepath of file to upload
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec file(String key, String filePath) {
        if (getLastRequest().getBody() == null) {
            getLastRequest().setBody(new PostyBody());
        }
        getLastRequest().getBody().addFile(key, filePath);
        return this;
    }

    /**
     * Creation of body, adding a parameters and files together
     * @param parameters Map of key-value pramethers
     * @param files Arraylist of files to upload
     * @return instance of PostyRequestDec, wich store the current request(s)
     */
    public PostyRequestDec body(Map<String, String> parameters, List<PostyFile> files) {
        getLastRequest().setBody(new PostyBody(parameters, files));
        return this;
    }




}

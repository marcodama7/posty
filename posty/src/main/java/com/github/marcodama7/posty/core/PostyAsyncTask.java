package com.github.marcodama7.posty.core;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.marcodama7.posty.Posty;
import com.github.marcodama7.posty.listeners.PostyMultipleResponseListener;
import com.github.marcodama7.posty.request.PostyFile;
import com.github.marcodama7.posty.enums.PostyMethod;
import com.github.marcodama7.posty.request.PostyRequest;
import com.github.marcodama7.posty.request.PostyResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AsyncTask, for execute in background thread the http call
 */
public class PostyAsyncTask extends AsyncTask<PostyRequest, String, PostyResponse[]> {

    private static final String RETRIEVE_COOKIES_HEADER = "Set-Cookie";
    private static final String ADD_COOKIES_HEADER = "Cookie";

    HttpURLConnection connection = null;
    List<FileInputStream> fileInputStreams = new ArrayList<>();

    PostyMultipleResponseListener postyMultipleResponseListener;

    public PostyMultipleResponseListener getPostyMultipleResponseListener() {
        return postyMultipleResponseListener;
    }

    public void setPostyMultipleResponseListener(PostyMultipleResponseListener postyMultipleResponseListener) {
        this.postyMultipleResponseListener = postyMultipleResponseListener;
    }

    // Send Http Request with NO body
    private PostyResponse sendWithNoBody(PostyRequest request) throws Exception {
        PostyResponse response = new PostyResponse();
        URL url = new URL(request.getUri());
        connection = (HttpURLConnection) url.openConnection();

        int timeout = (request.getTimeoutMillisecond() < 1) ? 10000 : request.getTimeoutMillisecond();

        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);


        // adding cookie
        if (Posty.getCookieManager().getCookieStore().getCookies() != null && Posty.getCookieManager().getCookieStore().getCookies().size() >0) {
            URI uri = request.getBaseURI();
            if (uri != null) {
                List<HttpCookie> cookies = Posty.getCookieManager().getCookieStore().get(uri);
                if (cookies != null) {
                    connection.setRequestProperty(ADD_COOKIES_HEADER, TextUtils.join(",", cookies));
                }
            }
        }

        // optional default is GET
        connection.setRequestMethod(request.getMethod().getRawMethod());
        // adding the headers ! (if exists)
        if (request.getHeaders() != null && request.getHeaders().size() > 0) {
            for (String key : request.getHeaders().keySet()) {
                String value = request.getHeaders().get(key);
                if (value != null) {
                    connection.setRequestProperty(key, value);
                }
            }
        }
        if (!request.hasNoResponse()) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseStringBuffer = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseStringBuffer.append(inputLine);
            }
            in.close();
            response.setResponse(responseStringBuffer.toString());
        }
        else {
            response.setResponse(null);
        }
        response.setError(null);
        response.setHeaders(connection.getHeaderFields());
        response.setOriginalRequest(request);

        // Reading cookie

        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(RETRIEVE_COOKIES_HEADER);

        if(cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                URI uri = request.getBaseURI();
                if (uri != null) {
                    List<HttpCookie> cookies = HttpCookie.parse(cookie);
                    if (cookies != null) {
                        for (HttpCookie httpCookie : cookies) {
                            Posty.getCookieManager().getCookieStore().add(uri, httpCookie);
                        }
                    }
                }
            }
        }
        // set response code
        response.setResponseCode(connection.getResponseCode());
        return response;
    }

    // Send Http Request with NO body
    private PostyResponse sendWithBody(PostyRequest request) throws Exception {
        PostyResponse response = new PostyResponse();
        URL url = new URL(request.getUri());
        connection = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(false);

        int timeout = (request.getTimeoutMillisecond() < 1) ? 10000 : request.getTimeoutMillisecond();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);

        // adding cookie
        if(Posty.getCookieManager().getCookieStore().get(new URI(request.getUri())).size() > 0)  {
            URI uri = request.getBaseURI();
            if (uri !=  null) {
                connection.setRequestProperty(ADD_COOKIES_HEADER, TextUtils.join(",", Posty.getCookieManager().getCookieStore().get(uri)));
            }
        }


        DataOutputStream outputStream;
        InputStream inputStream;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";
        String result;
        // Http Method
        switch (request.getMethod()) {
            case POST:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
            case DELETE:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
            case PUT:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
            case HEAD:
                connection.setDoInput(true);
                connection.setUseCaches(false);
                break;
            case OPTIONS:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
            case TRACE:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
            case PATCH:
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                break;
        }
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestMethod(request.getMethod().getRawMethod());
        /* Content Type */
        if (request.getHeaders() == null || request.getHeaders().size() > 0) {
            switch (request.getBody().getBodyType()) {
                case BODY_MULTIPART:
                case BODY_FORM_DATA:
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    break;
                case BODY_URLENCODED_FORM_DATA:
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    break;
                case BODY_JSONOBJECT:
                    connection.setRequestProperty("Content-Type", "application/json");
                    break;
            }
            // adding the headers ! (if exists)
            if (request.getHeaders() != null) {
                for (String key : request.getHeaders().keySet()) {
                    String value = request.getHeaders().get(key);
                    if (value != null) {
                        connection.setRequestProperty(key, value);
                    }
                }
            }
        }

        outputStream = new DataOutputStream(connection.getOutputStream());
        /* output Stream */
        switch (request.getBody().getBodyType()) {
            case BODY_URLENCODED_FORM_DATA:
            case BODY_JSONOBJECT:
            case BODY_CUSTOM:
                outputStream.writeBytes(request.getBody().getCustomBody());
                break;
            case BODY_MULTIPART:

                if (request.getBody().getParamethers().size() >0) {
                    for (String key : request.getBody().getParamethers().keySet()) {
                        String value = request.getBody().getParamethers().get(key);
                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                        outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(value);
                        outputStream.writeBytes(lineEnd);
                    }
                }

                if (request.getBody() != null && request.getBody().getFiles() != null && request.getBody().getFiles().size() > 0) {
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1024 * 1024;

                    for (PostyFile mpf : request.getBody().getFiles()) {
                        if (mpf == null || mpf.getFileKey() == null || mpf.getFilePath() == null)
                            continue;
                        String[] q = mpf.getFilePath().split("/");
                        int idx = q.length - 1;
                        File file = new File(mpf.getFilePath());
                        fileInputStreams.add(new FileInputStream(file));
                        outputStream = new DataOutputStream(connection.getOutputStream());
                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + mpf.getFileKey() + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
                        if (mpf.getMimeType().length()>0) {
                            outputStream.writeBytes("Content-Type: "+mpf.getMimeType() + lineEnd);
                        }
                        outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                        outputStream.writeBytes("Content-Length: " + file.length() + lineEnd);
                        outputStream.writeBytes(lineEnd);
                        bytesAvailable = fileInputStreams.get(fileInputStreams.size() - 1).available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];
                        bytesRead = fileInputStreams.get(fileInputStreams.size() - 1).read(buffer, 0, bufferSize);
                        while (bytesRead > 0) {
                            outputStream.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStreams.get(fileInputStreams.size() - 1).available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStreams.get(fileInputStreams.size() - 1).read(buffer, 0, bufferSize);
                        }
                        outputStream.writeBytes(lineEnd);
                        //outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    }
                    if (request.getBody().getFiles() != null && request.getBody().getFiles().size() > 0) {
                        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    }

                }
                break;
            case BODY_FORM_DATA:
                for (String key : request.getBody().getParamethers().keySet()) {
                    String value = request.getBody().getParamethers().get(key);
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(value);
                    outputStream.writeBytes(lineEnd);
                }
                break;
        }
        // Custom body
        if (!request.hasNoResponse()) {
            inputStream = connection.getInputStream();
            result = convertStreamToString(inputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            response.setResponse(result);

        }
        else {
            outputStream.flush();
            outputStream.close();
            response.setResponse(null);
        }
        response.setOriginalRequest(request);

        // Reading cookie
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(RETRIEVE_COOKIES_HEADER);
        if(cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                URI uri = request.getBaseURI();
                if (uri != null) {
                    List<HttpCookie> cookies = HttpCookie.parse(cookie);
                    if (cookies != null) {
                        for (HttpCookie httpCookie : cookies) {
                            Posty.getCookieManager().getCookieStore().add(uri, httpCookie);
                        }
                    }
                }
            }
        }

        response.setHeaders(connection.getHeaderFields());
        response.setError(null);
        // set response code
        response.setResponseCode(connection.getResponseCode());
        return response;
    }

    @Override
    protected PostyResponse[] doInBackground(PostyRequest... requests) {
        // size = number of http requests
        int size = (requests==null)? 0 : requests.length;
        PostyResponse[] responseBeans = new PostyResponse[size];
        if (requests != null && requests.length > 0)
            for (int i = 0; i < requests.length; i++) {
            PostyResponse responseBean = new PostyResponse();
            responseBean.setOriginalRequest(requests[i]);
            // Send Get Request
            try {
                if (requests[i].getMethod() == null || requests[i].getMethod().getRawMethod().equals(PostyMethod.GET.getRawMethod())) {
                    responseBean = sendWithNoBody(requests[i]);
                } else {
                    responseBean = sendWithBody(requests[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseBean.setError(e);
                if (connection != null) {
                    if (connection.getErrorStream() != null) {
                        responseBean.setResponse(convertStreamToString(connection.getErrorStream()));
                    }
                    // try to retrieve response code
                    try {
                        if (connection.getResponseCode() > 0) {
                            responseBean.setResponseCode(connection.getResponseCode());
                        }
                    } catch (IOException e1) {
                        // nothig to do
                        e1.printStackTrace();
                    }
                }
            } finally {
                if (fileInputStreams != null && fileInputStreams.size() > 0) {
                    for (FileInputStream fis : fileInputStreams) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            // nothing to do in this place
                        }
                    }
                }
                // disconnect
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }

            }
            responseBeans[i] = responseBean;
        }
        return responseBeans;
    }

    @Override
    protected void onPostExecute(PostyResponse[] results) {
        // UI thread
        super.onPostExecute(results);
        int numberOfErrors = 0;
        if (results != null) {
            for (int i=0; i<results.length; i++) {
                // check if is in error
                if (results[i] == null || results[i].inError()) {
                    numberOfErrors ++;
                }
                // for multiple calling: set previuos response (if exists)
                if (results[i] != null && i>0) {
                    results[i].setPreviousResponse(results[i-1]);
                }
                // send a response to a callback
                sendResponse(results[i]);
            }
            // if is setted a callbacks for multiple calls, calling this method passing arrays of results
            if (getPostyMultipleResponseListener() != null) {
                getPostyMultipleResponseListener().onResponse(results, numberOfErrors);
            }
        }
    }

    private void sendResponse(PostyResponse result){
        if (result != null && result.getOriginalRequest() != null){
            if (!result.inError() && result.getOriginalRequest().getPostyResponseListener() != null) {
                // success
                result.getOriginalRequest().getPostyResponseListener().onResponse(result);
            }
            else if (result.inError()) {
                // in error
                if (result.getOriginalRequest().getPostyErrorListener() != null) {
                    result.getOriginalRequest().getPostyErrorListener().onError(result);
                }
                else if (result.getOriginalRequest().getPostyResponseListener() != null) {
                    result.getOriginalRequest().getPostyResponseListener().onResponse(result);
                }
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
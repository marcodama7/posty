package com.marcodama7.posty.core;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.marcodama7.posty.message.PostyFile;
import com.marcodama7.posty.message.PostyRequest;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.marcodama7.posty.message.PostyResponse;
import com.marcodama7.posty.message.PostyBody;
import com.marcodama7.posty.util.UriUtils;

    /**
     * AsyncTask, for execute in background thread the http call
     */
    public class PostyAsyncTask extends AsyncTask<PostyRequest, String, PostyResponse[]> {

        private static final String RETRIEVE_COOKIES_HEADER = "Set-Cookie";
        private static final String ADD_COOKIES_HEADER = "Cookie";


        HttpURLConnection connection = null;
        List<FileInputStream> fileInputStreams = new ArrayList<>();
        // Send Http Request with NO body
        private PostyResponse sendWithNoBody(PostyRequest request) throws Exception {
            PostyResponse response = new PostyResponse();
            URL url = new URL(request.getUri());
            connection = (HttpURLConnection) url.openConnection();

            // adding cookie
            if(Posty.getCookieManager().getCookieStore().get(new URI(UriUtils.getBaseUri(request.getUri()))).size() > 0)  {
                connection.setRequestProperty(ADD_COOKIES_HEADER, TextUtils.join(",", Posty.getCookieManager().getCookieStore().get(new URI(UriUtils.getBaseUri(request.getUri())))));
            }

            // optional default is GET
            connection.setRequestMethod(request.getMethod());
            // adding the headers ! (if exists)
            if (request.getHeaders() != null && request.getHeaders().size() > 0) {
                Iterator<String> iMap = request.getHeaders().keySet().iterator();
                while (iMap.hasNext()) {
                    String key = iMap.next();
                    String value = request.getHeaders().get(key);
                    if (value != null) {
                        connection.setRequestProperty(key, value);
                    }
                }
            }
            int responseCode = connection.getResponseCode();
            if (!request.hasNoResponse()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer responseString = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseString.append(inputLine);
                }
                in.close();
                response.setResponse(responseString.toString());
            }
            else {
                response.setResponse(null);
            }
            response.setError(null);
            response.setResponseCode(responseCode);
            response.setHeaders(connection.getHeaderFields());
            response.setOriginalRequest(request);

            // Reading cookie
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(RETRIEVE_COOKIES_HEADER);
            if(cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    Posty.getCookieManager().getCookieStore().add(new URI(UriUtils.getBaseUri(request.getUri())), HttpCookie.parse(cookie).get(0));
                }
            }
            return response;
        }

        // Send Http Request with NO body
        private PostyResponse sendWithBody(PostyRequest request) throws Exception {
            PostyResponse response = new PostyResponse();
            URL url = new URL(request.getUri());
            connection = (HttpURLConnection) url.openConnection();
            int timeout = (request.getTimeoutMillisecond() < 1) ? 10000 : request.getTimeoutMillisecond();

            // adding cookie
            if(Posty.getCookieManager().getCookieStore().get(new URI(UriUtils.getBaseUri(request.getUri()))).size() > 0)  {
                connection.setRequestProperty(ADD_COOKIES_HEADER, TextUtils.join(",", Posty.getCookieManager().getCookieStore().get(new URI(UriUtils.getBaseUri(request.getUri())))));
            }

            connection.setConnectTimeout(timeout);
            DataOutputStream outputStream = null;
            InputStream inputStream = null;
            String twoHyphens = "--";
            String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
            String lineEnd = "\r\n";
            String result;
            // Http Method
            switch (request.getMethod()) {
                case PostyMethod.POST:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.DELETE:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.PUT:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.HEAD:
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.OPTIONS:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.TRACE:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
                case PostyMethod.PATCH:
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    break;
            }
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestMethod(request.getMethod());
            /* Content Type */
            if (request.getHeaders() == null || request.getHeaders().size() > 0) {
                switch (request.getBody().getBodyType()) {
                    case PostyBody.BODY_MULTIPART:
                    case PostyBody.BODY_FORM_DATA:
                        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                        break;
                    case PostyBody.BODY_URLENCODED_FORM_DATA:
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                        break;
                    case PostyBody.BODY_JSONOBJECT:
                        connection.setRequestProperty("Content-Type", "application/json");
                        break;
                }
                // adding the headers ! (if exists)
                if (request.getHeaders() != null) {
                    Iterator<String> iMap = request.getHeaders().keySet().iterator();
                    while (iMap.hasNext()) {
                        String key = iMap.next();
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
                case PostyBody.BODY_URLENCODED_FORM_DATA:
                case PostyBody.BODY_JSONOBJECT:
                case PostyBody.BODY_CUSTOM:
                    outputStream.writeBytes(request.getBody().getCustomBody());
                    break;
                case PostyBody.BODY_MULTIPART:

                    if (request.getBody().getParamethers().size() >0) {
                        Iterator<String> iMapMulti = request.getBody().getParamethers().keySet().iterator();
                        while (iMapMulti.hasNext()) {
                            String key = iMapMulti.next();
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
                        int maxBufferSize = 1 * 1024 * 1024;

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

                case PostyBody.BODY_FORM_DATA:
                    Iterator<String> iMap = request.getBody().getParamethers().keySet().iterator();
                    while (iMap.hasNext()) {
                        String key = iMap.next();
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
                response.setError(null);
                response.setResponseCode(connection.getResponseCode());
                response.setHeaders(connection.getHeaderFields());
            }
            else {
                outputStream.flush();
                outputStream.close();
                response.setResponse(null);
                response.setError(null);
                response.setResponseCode(connection.getResponseCode());
                response.setHeaders(connection.getHeaderFields());
            }
            response.setOriginalRequest(request);

            // Reading cookie
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(RETRIEVE_COOKIES_HEADER);
            if(cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    Posty.getCookieManager().getCookieStore().add(new URI(UriUtils.getBaseUri(request.getUri())), HttpCookie.parse(cookie).get(0));
                }
            }
            return response;
        }

        @Override
        protected PostyResponse[] doInBackground(PostyRequest... requests) {
            // size = number of http requests
            int size = (requests==null)? 0 : requests.length;
            PostyResponse[] responseBeans = new PostyResponse[size];
            if (requests != null && requests.length > 0) {
                for (int i=0; i<requests.length; i++) {
                    PostyResponse responseBean = new PostyResponse();
                    responseBean.setOriginalRequest(requests[i]);
                        // Send Get Request
                        try {
                            if (requests[i].getMethod() == PostyMethod.GET) {
                                responseBean = sendWithNoBody(requests[i]);
                            }
                            else {
                                responseBean = sendWithBody(requests[i]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseBean.setError(e);
                            String serverMessageError = "";
                            if (connection != null && connection.getErrorStream() != null) {
                                serverMessageError = convertStreamToString(connection.getErrorStream());
                            }
                            responseBean.setResponse(serverMessageError);
                        }
                        finally {
                            if (fileInputStreams != null && fileInputStreams.size() > 0) {
                                for (FileInputStream fis : fileInputStreams) {
                                    try {
                                        fis.close();
                                    }
                                    catch (IOException e) {
                                        // nothing to do in this place
                                    }
                                    fis = null;
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
                }
            return responseBeans;
        }

        @Override
        protected void onPostExecute(PostyResponse[] results) {
            super.onPostExecute(results);
            if (results != null) {
                for (int i=0; i<results.length; i++) {
                    sendResponse(results[i]);
                }
            }
            // UI thread
        }

        private void sendResponse(PostyResponse result){
            if (result != null && result.getOriginalRequest() != null && result.getOriginalRequest().getPostyResponseListener() != null) {
                    result.getOriginalRequest().getPostyResponseListener().onResponse(result);
            }
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
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
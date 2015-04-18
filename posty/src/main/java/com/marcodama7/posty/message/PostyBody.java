package com.marcodama7.posty.message;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Generic body of Http request.
 *
 */
public class PostyBody {

    public static final int BODY_NULL = 0;
    public static final int BODY_FORM_DATA = 1;
    public static final int BODY_URLENCODED_FORM_DATA = 2;
    public static final int BODY_MULTIPART = 3;
    public static final int BODY_JSONOBJECT = 4;
    public static final int BODY_CUSTOM = 5;

    private String customBody;  // custom body, for more customization
    private Map<String,String> paramethers; // pair key-values paramethers (for http request who accepts paramethers)
    private List<PostyAttachment> files; // pair key-values parahers for send files. PostyAttachment = wrapper for file
    private int bodyType = 0;   // type of body (Content-Type)

    public static PostyBody createCustom(String customBody, Map<String,String> paramethers, List<PostyAttachment> files, int bodyType){
        PostyBody body = new PostyBody();
        body.customBody = customBody;
        body.paramethers = paramethers;
        body.files = files;
        body.bodyType = bodyType;
        return body;
    }

    public int getBodyType() {

        return bodyType;
    }

    public PostyBody() {
        bodyType = BODY_NULL;
    }



    public PostyBody(Map<String, String> paramethers) {
        this(paramethers, false);
    }
    public PostyBody(Map<String, String> paramethers, boolean urlEncoded) {
        // for Map<String,String> ammissible bodyTypes are FORM_DATA or URLENCODED_FORM_DATA
        if (urlEncoded) {
            bodyType = BODY_URLENCODED_FORM_DATA;
            customBody = "";
            if (paramethers != null && !paramethers.isEmpty()) {
                Iterator<String> iMap = paramethers.keySet().iterator();
                while(iMap.hasNext()) {
                    String key = iMap.next();
                    String value = paramethers.get(key);
                    if (customBody != null && customBody.length() > 0) {
                        customBody += "&";
                    }
                    customBody +=key+"="+value;
                }
            }
        }
        else {
            bodyType = BODY_FORM_DATA;
            this.paramethers = paramethers;
        }
    }

    public PostyBody(String customBody) {
        this.customBody = customBody;
        bodyType = BODY_CUSTOM;
    }

    public PostyBody(JSONObject jsonObject) {
        bodyType = BODY_JSONOBJECT;
        customBody = jsonObject.toString();
    }

    public PostyBody(JSONArray jsonObject) {
        bodyType = BODY_JSONOBJECT;
        customBody = jsonObject.toString();
    }

    public PostyBody(List<PostyAttachment> files) {
        bodyType = BODY_MULTIPART;
        this.files = files;
    }

    public PostyBody(Map<String, String> paramethers, List<PostyAttachment> files) {
        bodyType = BODY_MULTIPART;
        this.files = files;
        this.paramethers = paramethers;
    }

    public boolean hasBody(){
        return  (customBody != null && customBody.length()>0)
                || (paramethers != null && paramethers.size() >0 )
                || (files != null && files.size() >0);
    }

    public String getCustomBody() {
        return customBody;
    }

    public Map<String, String> getParamethers() {
        if (paramethers == null) {
            paramethers = new HashMap<String,String>();
        }
        return paramethers;
    }

    public List<PostyAttachment> getFiles() {
        if (files == null) {
            files = new ArrayList<PostyAttachment>();
        }
        return files;
    }




}

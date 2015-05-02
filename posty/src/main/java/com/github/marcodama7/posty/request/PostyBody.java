package com.github.marcodama7.posty.request;
import com.github.marcodama7.posty.enums.BodyType;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic body of Http request.
 *
 */
public class PostyBody {

    private String customBody;  // custom body, for more customization
    private Map<String,String> paramethers; // pair key-values paramethers (for http request who accepts paramethers)
    private List<PostyFile> files; // pair key-values parahers for send files. PostyFile = wrapper for file
    private BodyType bodyType = BodyType.BODY_NULL;   // type of body (Content-Type)


    /**
     * Adding a custom body
     * @param customBody a custom body
     */
    public void addBody(String customBody) {
        this.customBody = customBody;
    }

    public void addBody(Map<String,String> paramethers) {
        if (this.paramethers == null || this.paramethers.size() < 1) {
            this.paramethers = paramethers;
        }
        else {
            this.paramethers.putAll(paramethers);
        }
        bodyType = (files == null || files.size() < 1) ? BodyType.BODY_NULL: BodyType.BODY_MULTIPART;
    }

    public void addBodyParam(String key, String value) {
        if (paramethers == null) paramethers = new HashMap<>();
        paramethers.put(key, value);
        bodyType = (files != null && files.size() > 0) ? BodyType.BODY_MULTIPART : BodyType.BODY_FORM_DATA;
    }

    public void addBodyParam(String paramUrlEncoded) {
        customBody = paramUrlEncoded;
        bodyType = BodyType.BODY_URLENCODED_FORM_DATA;
    }

    public void addBodyParamUrlEncoded(String key, String value) {
        customBody = (customBody == null || customBody.length() < 1) ? key+"="+value : "&"+key+"="+value;
        bodyType = BodyType.BODY_URLENCODED_FORM_DATA;
    }

    public void addBodyParamUrlEncoded(Map<String, String> values) {
        if (values != null) {
            for (String key : values.keySet()) {
                String value = values.get(key);
                addBodyParamUrlEncoded(key, value);
            }
        }
    }

    public void add(PostyFile file) {
        if (this.files == null) {
            this.files = new ArrayList<PostyFile>();
        }
        this.files.add(file);
        bodyType = BodyType.BODY_MULTIPART;
    }

    public void add(List<PostyFile> files) {
        if (this.files == null || this.files.size() < 1) {
            this.files = files;
        }
        else {
            this.files.addAll(files);
        }
        bodyType = BodyType.BODY_MULTIPART;
    }

    public void addFile(String key, String filename, String mimeType) {
        PostyFile postyFile = new PostyFile(key, filename, mimeType);
        add(postyFile);
    }

    public void addFile(String key, String filename) {
        PostyFile postyFile = new PostyFile(key, filename);
        add(postyFile);
    }

    public static PostyBody createCustom(String customBody, Map<String,String> paramethers, List<PostyFile> files, BodyType bodyType){
        PostyBody body = new PostyBody();
        body.customBody = customBody;
        body.paramethers = paramethers;
        body.files = files;
        body.bodyType = bodyType;
        return body;
    }

    public BodyType getBodyType() {

        return bodyType;
    }

    public PostyBody() {
        bodyType = BodyType.BODY_NULL;
    }

    public PostyBody(Map<String, String> paramethers) {
        this(paramethers, false);
    }
    public PostyBody(Map<String, String> paramethers, boolean urlEncoded) {
        // for Map<String,String> ammissible bodyTypes are FORM_DATA or URLENCODED_FORM_DATA
        if (urlEncoded) {
            bodyType = BodyType.BODY_URLENCODED_FORM_DATA;
            customBody = "";
            if (paramethers != null && !paramethers.isEmpty()) {
                for (String key : paramethers.keySet()) {
                    String value = paramethers.get(key);
                    if (customBody != null && customBody.length() > 0) {
                        customBody += "&";
                    }
                    customBody += key + "=" + value;
                }
            }
        }
        else {
            bodyType = BodyType.BODY_FORM_DATA;
            this.paramethers = paramethers;
        }
    }

    public PostyBody(String customBody) {
        this.customBody = customBody;
        bodyType = BodyType.BODY_CUSTOM;
    }

    public PostyBody(JSONObject jsonObject) {
        bodyType = BodyType.BODY_JSONOBJECT;
        customBody = jsonObject.toString();
    }

    public PostyBody(JSONArray jsonObject) {
        bodyType = BodyType.BODY_JSONOBJECT;
        customBody = jsonObject.toString();
    }

    public PostyBody(List<PostyFile> files) {
        bodyType = BodyType.BODY_MULTIPART;
        this.files = files;
    }

    public PostyBody(Map<String, String> paramethers, List<PostyFile> files) {
        bodyType = BodyType.BODY_MULTIPART;
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
            paramethers = new HashMap<>();
        }
        return paramethers;
    }

    public List<PostyFile> getFiles() {
        if (files == null) {
            files = new ArrayList<>();
        }
        return files;
    }

}

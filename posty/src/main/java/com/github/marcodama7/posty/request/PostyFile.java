package com.github.marcodama7.posty.request;

/**
 * File attachment in a Http request body
 */
public class PostyFile {

    String fileKey;     // key associated at file
    String filePath;    // filepath (absolute)
    String mimeType;    // optionally: mimetype
    //TODO: manage mimetype

    public PostyFile(String fileKey, String filePath) {
        this.fileKey = fileKey;
        this.filePath = filePath;
    }

    public PostyFile(String fileKey, String filePath, String mimeType) {
        this.fileKey = fileKey;
        this.filePath = filePath;
        this.mimeType = mimeType;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        if (mimeType == null) {
            mimeType = "image/jpeg";
        }
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}

package com.marcodama7.posty.message;

/**
 * Created by Marco on 15/03/2015.
 */
public class PostyAttachment {

    String fileKey;
    String filePath;
    String mimeType;

    public PostyAttachment(String fileKey, String filePath) {
        this.fileKey = fileKey;
        this.filePath = filePath;
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

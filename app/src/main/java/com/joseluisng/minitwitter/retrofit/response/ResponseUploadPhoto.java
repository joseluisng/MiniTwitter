
package com.joseluisng.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUploadPhoto {

    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("originalname")
    @Expose
    private String originalname;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private Integer size;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponseUploadPhoto() {
    }

    /**
     * 
     * @param mimetype
     * @param path
     * @param encoding
     * @param filename
     * @param size
     * @param destination
     * @param originalname
     */
    public ResponseUploadPhoto(String filename, String originalname, String encoding, String mimetype, String destination, String path, Integer size) {
        super();
        this.filename = filename;
        this.originalname = originalname;
        this.encoding = encoding;
        this.mimetype = mimetype;
        this.destination = destination;
        this.path = path;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}

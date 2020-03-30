package com.ryan.github.view;

import java.util.Map;

/**
 * Created by Ryan
 * 2018/3/1 下午5:40
 */
public class WebResource {

    private int responseCode;

    private String reasonPhrase;

    private Map<String, String> responseHeaders;

    private boolean isModified = true;

    private boolean isCache = false;

    private byte[] originBytes;

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public boolean isModified() {
        return isModified;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setOriginBytes(byte[] bytes) {
        this.originBytes = bytes;
    }

    public byte[] getOriginBytes() {
        return originBytes;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}

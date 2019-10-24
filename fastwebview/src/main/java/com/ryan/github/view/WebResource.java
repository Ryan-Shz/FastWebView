package com.ryan.github.view;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Ryan
 * 2018/3/1 下午5:40
 */
public class WebResource {

    private ReusableInputStream inputStream;

    private Map<String, List<String>> responseHeaders;

    private boolean isModified = true;

    private boolean isCache = false;

    private byte[] mOriginBytes;

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public boolean isModified() {
        return isModified;
    }

    public ReusableInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ReusableInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setOriginBytes(byte[] bytes) {
        this.mOriginBytes = bytes;
    }

    public boolean newResource() {
        if (mOriginBytes != null) {
            this.inputStream = new ReusableInputStream(new ByteArrayInputStream(mOriginBytes));
            return true;
        }
        return false;
    }

    public byte[] getOriginBytes() {
        return mOriginBytes;
    }
}

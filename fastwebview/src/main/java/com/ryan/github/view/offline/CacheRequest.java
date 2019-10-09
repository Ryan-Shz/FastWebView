package com.ryan.github.view.offline;

import java.util.Map;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class CacheRequest {

    private String url;
    private String mime;
    private String extension;
    private boolean forceMode;
    private Map<String, String> mHeaders;
    private String mUserAgent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setForceMode(boolean forceMode) {
        this.forceMode = forceMode;
    }

    public boolean isForceMode() {
        return forceMode;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public String getUserAgent() {
        return mUserAgent;
    }

    public void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
    }
}

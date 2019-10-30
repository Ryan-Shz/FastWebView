package com.ryan.github.view.loader;

import com.ryan.github.view.offline.CacheRequest;

import java.util.Map;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class SourceRequest {

    private String url;
    private boolean cacheable;
    private Map<String, String> headers;
    private String userAgent;
    private int webViewCache;

    public SourceRequest(CacheRequest request, boolean cacheable){
        this.cacheable = cacheable;
        this.url = request.getUrl();
        this.headers = request.getHeaders();
        this.userAgent = request.getUserAgent();
        this.webViewCache = request.getWebViewCacheMode();
    }

    public String getUrl() {
        return url;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getWebViewCache() {
        return webViewCache;
    }

    public void setWebViewCache(int webViewCache) {
        this.webViewCache = webViewCache;
    }
}

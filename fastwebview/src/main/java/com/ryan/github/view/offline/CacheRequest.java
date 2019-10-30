package com.ryan.github.view.offline;

import com.ryan.github.view.utils.MD5Utils;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class CacheRequest {

    private String key;
    private String url;
    private String mime;
    private boolean forceMode;
    private Map<String, String> mHeaders;
    private String mUserAgent;
    private int mWebViewCacheMode;

    public String getKey() {
        return key;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.key = generateKey(url);
    }

    public void setWebViewCacheMode(int webViewCacheMode) {
        this.mWebViewCacheMode = webViewCacheMode;
    }

    public int getWebViewCacheMode() {
        return mWebViewCacheMode;
    }

    private static String generateKey(String url) {
        return MD5Utils.getMD5(URLEncoder.encode(url), false);
    }
}

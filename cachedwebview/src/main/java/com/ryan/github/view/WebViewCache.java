package com.ryan.github.view;

import android.webkit.WebResourceResponse;

import com.ryan.github.view.offline.ResourceInterceptor;

import java.util.Map;

/**
 * Created by Ryan
 * 2018/2/7 下午5:06
 */
public interface WebViewCache {

    WebResourceResponse getResource(String url);

    void destroyResource();

    void openForceMode();

    void addResourceInterceptor(ResourceInterceptor interceptor);

    void addHeader(String url, Map<String, String> header);

    void setUserAgent(String userAgent);
}

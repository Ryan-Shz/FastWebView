package com.ryan.github.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.offline.CacheRequest;
import com.ryan.github.view.offline.OfflineServer;
import com.ryan.github.view.offline.OfflineServerImpl;
import com.ryan.github.view.offline.ResourceInterceptor;
import com.ryan.github.view.utils.MimeTypeMapUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ryan
 * 2018/2/7 下午5:07
 */
public class WebViewCacheImpl implements WebViewCache {

    private boolean mForceMode = false;
    private Map<String, Map<String, String>> mHeaders;
    private OfflineServer mOfflineServer;
    private String mUserAgent;

    WebViewCacheImpl(Context context, CacheConfig cacheConfig) {
        mHeaders = new ConcurrentHashMap<>();
        mOfflineServer = new OfflineServerImpl(context, cacheConfig);
    }

    @Override
    public WebResourceResponse getResource(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            return null;
        }
        String extension = MimeTypeMapUtils.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMapUtils.getMimeTypeFromExtension(extension);
        CacheRequest request = new CacheRequest();
        request.setUrl(url);
        request.setMime(mimeType);
        request.setExtension(extension);
        request.setForceMode(mForceMode);
        request.setUserAgent(mUserAgent);
        Map<String, String> headers = mHeaders.get(formatUrl(url));
        request.setHeaders(headers);
        return mOfflineServer.get(request);
    }

    @Override
    public void destroyResource() {
        mOfflineServer.destroy();
    }

    @Override
    public void addHeader(String url, Map<String, String> header) {
        if (TextUtils.isEmpty(url) || header == null || header.isEmpty()) {
            return;
        }
        mHeaders.put(formatUrl(url), header);
    }

    @Override
    public void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    @Override
    public void openForceMode() {
        mForceMode = true;
    }

    @Override
    public void addResourceInterceptor(ResourceInterceptor interceptor) {
        mOfflineServer.addResourceInterceptor(interceptor);
    }

    private String formatUrl(String url) {
        return url.replaceAll("/+$", "");
    }
}

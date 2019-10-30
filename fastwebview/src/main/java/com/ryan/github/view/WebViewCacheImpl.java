package com.ryan.github.view;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.offline.CacheRequest;
import com.ryan.github.view.offline.OfflineServer;
import com.ryan.github.view.offline.OfflineServerImpl;
import com.ryan.github.view.offline.ResourceInterceptor;
import com.ryan.github.view.utils.AppVersionUtil;
import com.ryan.github.view.utils.MimeTypeMapUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ryan
 * 2018/2/7 下午5:07
 */
public class WebViewCacheImpl implements WebViewCache {

    private static final String CACHE_DIR_NAME = "cached_webview_force";
    private static final int DEFAULT_DISK_CACHE_SIZE = 100 * 1024 * 1024;
    private boolean mForceMode = false;
    private Map<String, Map<String, String>> mHeaders;
    private OfflineServer mOfflineServer;
    private String mUserAgent;

    WebViewCacheImpl(Context context, CacheConfig cacheConfig) {
        mHeaders = new ConcurrentHashMap<>();
        cacheConfig = generateCacheConfig(context, cacheConfig);
        mOfflineServer = new OfflineServerImpl(context, cacheConfig);
    }

    @Override
    public WebResourceResponse getResource(String url, int cacheMode) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            return null;
        }
        String extension = MimeTypeMapUtils.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMapUtils.getMimeTypeFromExtension(extension);
        CacheRequest request = new CacheRequest();
        request.setUrl(url);
        request.setMime(mimeType);
        request.setForceMode(mForceMode);
        request.setUserAgent(mUserAgent);
        request.setWebViewCacheMode(cacheMode);
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

    private CacheConfig generateCacheConfig(Context context, CacheConfig userConfig) {
        if (userConfig != null) {
            return userConfig;
        }
        String dir = context.getCacheDir() + File.separator + CACHE_DIR_NAME;
        return new CacheConfig.Builder()
                .setExtensionFilter(new DefaultMimeTypeFilter())
                .setDiskCacheSize(DEFAULT_DISK_CACHE_SIZE)
                .setVersion(AppVersionUtil.getVersionCode(context))
                .setCacheDir(dir)
                .build();
    }
}
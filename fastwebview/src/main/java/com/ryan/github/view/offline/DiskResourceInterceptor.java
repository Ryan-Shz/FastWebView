package com.ryan.github.view.offline;

import android.text.TextUtils;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.CacheConfig;
import com.ryan.github.view.lru.DiskLruCache;
import com.ryan.github.view.utils.LogUtils;
import com.ryan.github.view.ReusableInputStream;
import com.ryan.github.view.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class DiskResourceInterceptor implements Destroyable, ResourceInterceptor {

    private static final int ENTRY_META = 0;
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private DiskLruCache mDiskLruCache;
    private CacheConfig mCacheConfig;

    DiskResourceInterceptor(CacheConfig cacheConfig) {
        mCacheConfig = cacheConfig;
    }

    private synchronized void ensureDiskLruCacheCreate() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            return;
        }
        String dir = mCacheConfig.getCacheDir();
        int version = mCacheConfig.getVersion();
        long cacheSize = mCacheConfig.getDiskCacheSize();
        try {
            mDiskLruCache = DiskLruCache.open(new File(dir), version, ENTRY_COUNT, cacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WebResource getFromDiskCache(String url) {
        try {
            if (mDiskLruCache.isClosed()) {
                return null;
            }
            String key = getKey(url);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                // 1. read headers
                BufferedSource entrySource = Okio.buffer(Okio.source(snapshot.getInputStream(ENTRY_META)));
                long headerSize = entrySource.readDecimalLong();
                Map<String, List<String>> headers;
                Headers.Builder responseHeadersBuilder = new Headers.Builder();
                for (int i = 0; i < headerSize; i++) {
                    String line = entrySource.readUtf8LineStrict();
                    if (!TextUtils.isEmpty(line)) {
                        responseHeadersBuilder.add(line);
                    }
                }
                headers = responseHeadersBuilder.build().toMultimap();
                // 2. read body
                InputStream inputStream = snapshot.getInputStream(ENTRY_BODY);
                if (inputStream != null) {
                    WebResource webResource = new WebResource();
                    webResource.setInputStream(new ReusableInputStream(inputStream));
                    webResource.setResponseHeaders(headers);
                    webResource.setModified(false);
                    return webResource;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getKey(String url) {
        return MD5Utils.getMD5(URLEncoder.encode(url), false);
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        ensureDiskLruCacheCreate();
        WebResource webResource = getFromDiskCache(request.getUrl());
        if (webResource != null) {
            LogUtils.d(String.format("disk cache hit: %s", request.getUrl()));
            return webResource;
        }
        webResource = chain.process(request);
        if (webResource != null && (webResource.isCache() || isRealMimeTypeCacheable(webResource))) {
            cacheToDisk(request.getUrl(), webResource);
        }
        return webResource;
    }

    @Override
    public void destroy() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cacheToDisk(String url, WebResource webResource) {
        if (webResource == null || webResource.getInputStream() == null) {
            return;
        }
        try {
            InputStream inputStream = webResource.getInputStream();
            inputStream.reset();
            if (mDiskLruCache.isClosed()) {
                return;
            }
            String urlKey = getKey(url);
            DiskLruCache.Editor editor = mDiskLruCache.edit(urlKey);
            // 1. write response header
            Map<String, List<String>> headers = webResource.getResponseHeaders();
            OutputStream metaOutput = editor.newOutputStream(ENTRY_META);
            BufferedSink sink = Okio.buffer(Okio.sink(metaOutput));
            sink.writeDecimalLong(headers.size()).writeByte('\n');
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0);
                sink.writeUtf8(key)
                        .writeUtf8(": ")
                        .writeUtf8(value)
                        .writeByte('\n');
            }
            sink.flush();
            sink.close();
            // 2. write response body
            OutputStream bodyOutput = editor.newOutputStream(ENTRY_BODY);
            sink = Okio.buffer(Okio.sink(bodyOutput));
            sink.writeAll(Okio.source(inputStream));
            sink.flush();
            sink.close();
            editor.commit();
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isRealMimeTypeCacheable(WebResource resource) {
        if (resource == null) {
            return false;
        }
        Map<String, List<String>> headers = resource.getResponseHeaders();
        String contentType = null;
        if (headers != null) {
            String contentTypeKey = "content-type";
            if (headers.containsKey(contentTypeKey)) {
                List<String> contentTypeList = headers.get(contentTypeKey);
                if (contentTypeList != null && !contentTypeList.isEmpty()) {
                    String rawContentType = contentTypeList.get(0);
                    String[] contentTypeArray = rawContentType.split(";");
                    if (contentTypeArray.length >= 1) {
                        contentType = contentTypeArray[0];
                    }
                }
            }
        }
        return !mCacheConfig.getFilter().isFilter(contentType);
    }
}

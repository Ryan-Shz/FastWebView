package com.ryan.github.view.offline;

import android.content.Context;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.utils.AppVersionUtil;
import com.ryan.github.view.lru.DiskLruCache;
import com.ryan.github.view.CacheConfig;
import com.ryan.github.view.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class DiskResourceInterceptor implements Destroyable, ResourceInterceptor {

    private static final String CACHE_DIR_NAME = "cached_webview_force";
    private static final int DEFAULT_DISK_CACHE_SIZE = 100 * 1024 * 1024;
    private static final int ENTRY_BODY = 0;
    private static final int ENTRY_COUNT = 1;
    private DiskLruCache mDiskLruCache;
    private Context mContext;
    private CacheConfig mCacheConfig;

    DiskResourceInterceptor(Context context, CacheConfig cacheConfig) {
        mContext = context;
        mCacheConfig = cacheConfig;
    }

    private synchronized void ensureDiskLruCacheCreate() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            return;
        }
        String dir;
        int version;
        long cacheSize;
        if (mCacheConfig != null) {
            dir = mCacheConfig.getCacheDir();
            version = mCacheConfig.getVersion();
            cacheSize = mCacheConfig.getDiskCacheSize();
        } else {
            dir = mContext.getCacheDir() + File.separator + CACHE_DIR_NAME;
            version = AppVersionUtil.getVersionCode(mContext);
            cacheSize = DEFAULT_DISK_CACHE_SIZE;
        }
        try {
            mDiskLruCache = DiskLruCache.open(new File(dir), version, ENTRY_COUNT, cacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getFromDiskCache(String url) {
        InputStream inputStream = null;
        try {
            if (mDiskLruCache.isClosed()) {
                return null;
            }
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(getKey(url));
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(ENTRY_BODY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private static String getKey(String url) {
        return MD5Utils.getMD5(URLEncoder.encode(url), false);
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        ensureDiskLruCacheCreate();
        InputStream ins = getFromDiskCache(request.getUrl());
        if (ins != null) {
            WebResource webResource = new WebResource();
            webResource.setInputStream(ins);
            webResource.setModified(false);
            return webResource;
        }
        WebResource resource = chain.process(request);
        if (resource != null && resource.isCache()) {
            cacheToDisk(request.getUrl(), resource.getInputStream());
        }
        return resource;
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

    private void cacheToDisk(String url, InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.reset();
            if (mDiskLruCache.isClosed()) {
                return;
            }
            String key = getKey(url);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            while (len != -1) {
                outputStream.write(buffer, 0, len);
                len = inputStream.read(buffer);
            }
            outputStream.flush();
            outputStream.close();
            editor.commit();
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

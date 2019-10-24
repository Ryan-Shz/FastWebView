package com.ryan.github.view.offline;

import android.util.LruCache;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.utils.LogUtils;


/**
 * Created by Ryan
 * on 2019/10/24
 */
public class MemoryCacheInterceptor implements ResourceInterceptor {

    private LruCache<String, WebResource> mMemoryCache;
    private int maxMemorySize = calMemoryCacheSize();

    static MemoryCacheInterceptor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final MemoryCacheInterceptor INSTANCE = new MemoryCacheInterceptor();
    }

    private MemoryCacheInterceptor() {

    }

    public void setMaxMemorySize(int maxMemorySize) {
        this.maxMemorySize = maxMemorySize;
    }

    @Override
    public WebResource load(Chain chain) {
        ensureLruCacheCreated();
        CacheRequest request = chain.getRequest();
        String key = request.getKey();
        WebResource resource = mMemoryCache.get(key);
        if (resource != null && resource.newResource()) {
            LogUtils.d(String.format("memory cache hit: %s", request.getUrl()));
            return resource;
        }
        resource = chain.process(request);
        if (resource != null) {
            mMemoryCache.put(key, resource);
        }
        return resource;
    }

    private synchronized void ensureLruCacheCreated() {
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<>(maxMemorySize);
        }
    }

    private int calMemoryCacheSize() {
        int size = (int) (Runtime.getRuntime().maxMemory() / 16);
        int defaultMinSize = 5 * 1024 * 1024;
        int defaultMaxSize = 30 * 1024 * 1024;
        return Math.max(defaultMinSize, Math.min(defaultMaxSize, size));
    }
}

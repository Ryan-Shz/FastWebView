package com.ryan.github.view.offline;

import android.support.v4.util.LruCache;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.config.CacheConfig;

/**
 * Created by Ryan
 * on 2020/4/14
 */
public class MemResourceInterceptor implements ResourceInterceptor, Destroyable {

    private LruCache<String, WebResource> mLruCache;

    private static volatile MemResourceInterceptor sInstance;

    public static MemResourceInterceptor getInstance(CacheConfig cacheConfig) {
        if (sInstance == null) {
            synchronized (MemResourceInterceptor.class) {
                if (sInstance == null) {
                    sInstance = new MemResourceInterceptor(cacheConfig);
                }
            }
        }
        return sInstance;
    }

    private MemResourceInterceptor(CacheConfig cacheConfig) {
        int memorySize = cacheConfig.getMemCacheSize();
        if (memorySize > 0) {
            mLruCache = new ResourceMemCache(memorySize);
        }
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        if (mLruCache != null) {
            WebResource resource = mLruCache.get(request.getKey());
            if (checkResourceValid(resource)) {
                return resource;
            }
        }
        WebResource resource = chain.process(request);
        if (mLruCache != null && checkResourceValid(resource) && resource.isCacheable()) {
            mLruCache.put(request.getKey(), resource);
        }
        return resource;
    }

    private boolean checkResourceValid(WebResource resource) {
        return resource != null
                && resource.getOriginBytes() != null
                && resource.getOriginBytes().length >= 0
                && resource.getResponseHeaders() != null
                && !resource.getResponseHeaders().isEmpty();
    }

    @Override
    public void destroy() {
        if (mLruCache != null) {
            mLruCache.evictAll();
            mLruCache = null;
        }
    }

    private static class ResourceMemCache extends LruCache<String, WebResource> {

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        ResourceMemCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, WebResource value) {
            int size = 0;
            if (value != null && value.getOriginBytes() != null) {
                size = value.getOriginBytes().length;
            }
            return size;
        }
    }
}

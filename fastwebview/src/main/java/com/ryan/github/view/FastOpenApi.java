package com.ryan.github.view;

import com.ryan.github.view.config.CacheConfig;
import com.ryan.github.view.config.FastCacheMode;
import com.ryan.github.view.offline.ResourceInterceptor;

/**
 * Created by Ryan
 * at 2019/11/1
 */
public interface FastOpenApi {

    void setCacheMode(FastCacheMode mode, CacheConfig cacheConfig);

    void addResourceInterceptor(ResourceInterceptor interceptor);
}

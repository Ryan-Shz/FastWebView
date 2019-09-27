package com.ryan.github.view.offline;

import android.webkit.WebResourceResponse;

import java.util.List;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class Chain {

    private List<ResourceInterceptor> mInterceptors;
    private int mIndex = -1;
    private CacheRequest mRequest;

    Chain(List<ResourceInterceptor> interceptors) {
        mInterceptors = interceptors;
    }

    public WebResourceResponse process(CacheRequest request) {
        mRequest = request;
        ResourceInterceptor interceptor = mInterceptors.get(++mIndex);
        return interceptor.load(this);
    }

    public CacheRequest getRequest() {
        return mRequest;
    }
}

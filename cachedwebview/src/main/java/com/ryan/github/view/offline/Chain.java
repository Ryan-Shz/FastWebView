package com.ryan.github.view.offline;

import com.ryan.github.view.WebResource;

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

    public WebResource process(CacheRequest request) {
        if (++mIndex >= mInterceptors.size()) {
            return null;
        }
        mRequest = request;
        ResourceInterceptor interceptor = mInterceptors.get(mIndex);
        return interceptor.load(this);
    }

    public CacheRequest getRequest() {
        return mRequest;
    }
}

package com.ryan.github.view.offline;

import android.content.Context;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.CacheConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class OfflineServerImpl implements OfflineServer {

    private Context mContext;
    private CacheConfig mCacheConfig;
    private List<ResourceInterceptor> mBaseInterceptorList;
    private List<ResourceInterceptor> mForceModeChainList;
    private List<ResourceInterceptor> mDefaultModeChainList;

    public OfflineServerImpl(Context context, CacheConfig cacheConfig) {
        mContext = context.getApplicationContext();
        mCacheConfig = cacheConfig;
    }

    private synchronized List<ResourceInterceptor> buildForceModeChain(Context context, CacheConfig cacheConfig) {
        if (mForceModeChainList == null) {
            List<ResourceInterceptor> interceptors = new ArrayList<>();
            interceptors.addAll(mBaseInterceptorList);
            interceptors.add(new DiskResourceInterceptor(context, cacheConfig));
            interceptors.add(new ForceRemoteResourceInterceptor(context, cacheConfig));
            mForceModeChainList = interceptors;
        }
        return mForceModeChainList;
    }

    private synchronized List<ResourceInterceptor> buildDefaultModeChain(Context context) {
        if (mDefaultModeChainList == null) {
            List<ResourceInterceptor> interceptors = new ArrayList<>();
            interceptors.addAll(mBaseInterceptorList);
            interceptors.add(new DefaultRemoteResourceInterceptor(context));
            mDefaultModeChainList = interceptors;
        }
        return mDefaultModeChainList;
    }

    @Override
    public WebResourceResponse get(CacheRequest request) {
        boolean isForceMode = request.isForceMode();
        Context context = mContext;
        CacheConfig config = mCacheConfig;
        List<ResourceInterceptor> interceptors = isForceMode ? buildForceModeChain(context, config) : buildDefaultModeChain(context);
        return callChain(interceptors, request);
    }

    @Override
    public synchronized void addResourceInterceptor(ResourceInterceptor interceptor) {
        if (mBaseInterceptorList == null) {
            mBaseInterceptorList = new ArrayList<>();
        }
        mBaseInterceptorList.add(interceptor);
    }

    @Override
    public void destroy() {
        destroyAll(mDefaultModeChainList);
        destroyAll(mForceModeChainList);
    }

    private WebResourceResponse callChain(List<ResourceInterceptor> interceptors, CacheRequest request) {
        Chain chain = new Chain(interceptors);
        return chain.process(request);
    }

    private void destroyAll(List<ResourceInterceptor> interceptors) {
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        for (ResourceInterceptor interceptor : interceptors) {
            if (interceptor instanceof Destroyable) {
                ((Destroyable) interceptor).destroy();
            }
        }
    }
}

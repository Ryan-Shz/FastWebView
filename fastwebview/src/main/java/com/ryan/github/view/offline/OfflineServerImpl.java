package com.ryan.github.view.offline;

import android.content.Context;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.CacheConfig;
import com.ryan.github.view.WebResource;

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
    private WebResourceResponseGenerator mResourceResponseGenerator;

    public OfflineServerImpl(Context context, CacheConfig cacheConfig) {
        mContext = context.getApplicationContext();
        mCacheConfig = cacheConfig;
        mResourceResponseGenerator = new DefaultWebResponseGenerator();
    }

    private List<ResourceInterceptor> buildForceModeChain(Context context, CacheConfig cacheConfig) {
        if (mForceModeChainList == null) {
            int interceptorsCount = 1 + getBaseInterceptorsCount();
            List<ResourceInterceptor> interceptors = new ArrayList<>(interceptorsCount);
            if (mBaseInterceptorList != null && !mBaseInterceptorList.isEmpty()) {
                interceptors.addAll(mBaseInterceptorList);
            }
            interceptors.add(new DiskResourceInterceptor(cacheConfig));
            interceptors.add(new ForceRemoteResourceInterceptor(context, cacheConfig));
            mForceModeChainList = interceptors;
        }
        return mForceModeChainList;
    }

    private List<ResourceInterceptor> buildDefaultModeChain(Context context) {
        if (mDefaultModeChainList == null) {
            int interceptorsCount = 1 + getBaseInterceptorsCount();
            List<ResourceInterceptor> interceptors = new ArrayList<>(interceptorsCount);
            if (mBaseInterceptorList != null && !mBaseInterceptorList.isEmpty()) {
                interceptors.addAll(mBaseInterceptorList);
            }
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
        WebResource resource = callChain(interceptors, request);
        return mResourceResponseGenerator.generate(resource, request.getMime());
    }

    @Override
    public synchronized void addResourceInterceptor(ResourceInterceptor interceptor) {
        if (mBaseInterceptorList == null) {
            mBaseInterceptorList = new ArrayList<>();
        }
        mBaseInterceptorList.add(interceptor);
    }

    @Override
    public synchronized void destroy() {
        destroyAll(mDefaultModeChainList);
        destroyAll(mForceModeChainList);
    }

    private WebResource callChain(List<ResourceInterceptor> interceptors, CacheRequest request) {
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

    private int getBaseInterceptorsCount() {
        return mBaseInterceptorList == null ? 0 : mBaseInterceptorList.size();
    }
}

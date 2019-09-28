package com.ryan.github.view.offline;

import android.content.Context;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.loader.OkHttpResourceLoader;
import com.ryan.github.view.loader.ResourceLoader;
import com.ryan.github.view.loader.SourceRequest;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class DefaultRemoteResourceInterceptor implements ResourceInterceptor {

    private ResourceLoader mResourceLoader;

    DefaultRemoteResourceInterceptor(Context context) {
        mResourceLoader = new OkHttpResourceLoader(context);
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        SourceRequest sourceRequest = new SourceRequest(request.getUrl(), true, request.getHeaders());
        sourceRequest.setUserAgent(request.getUserAgent());
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        if (resource != null) {
            return resource;
        }
        return chain.process(request);
    }

}

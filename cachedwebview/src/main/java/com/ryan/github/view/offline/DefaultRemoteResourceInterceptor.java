package com.ryan.github.view.offline;

import android.content.Context;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.loader.OkHttpResourceLoader;
import com.ryan.github.view.loader.ResourceLoader;
import com.ryan.github.view.loader.SourceRequest;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class DefaultRemoteResourceInterceptor extends BaseResourceInterceptor {

    private ResourceLoader mResourceLoader;

    DefaultRemoteResourceInterceptor(Context context) {
        mResourceLoader = new OkHttpResourceLoader(context);
    }

    @Override
    public WebResourceResponse load(Chain chain) {
        CacheRequest request = chain.getRequest();
        SourceRequest sourceRequest = new SourceRequest(request.getUrl(), true, request.getHeaders());
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        WebResourceResponse response = generateWebResourceResponse(resource, request.getMime());
        if (response != null) {
            return response;
        }
        return chain.process(request);
    }

}

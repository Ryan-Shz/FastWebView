package com.ryan.github.view.offline;

import android.content.Context;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.CacheConfig;
import com.ryan.github.view.DefaultExtensionFilter;
import com.ryan.github.view.ExtensionFilter;
import com.ryan.github.view.WebResource;
import com.ryan.github.view.loader.OkHttpResourceLoader;
import com.ryan.github.view.loader.ResourceLoader;
import com.ryan.github.view.loader.SourceRequest;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class ForceRemoteResourceInterceptor extends BaseResourceInterceptor implements Destroyable {

    private ResourceLoader mResourceLoader;
    private ExtensionFilter mExtensionFilter;

    ForceRemoteResourceInterceptor(Context context, CacheConfig cacheConfig) {
        mResourceLoader = new OkHttpResourceLoader(context);
        mExtensionFilter = cacheConfig != null ? cacheConfig.getFilter() : null;
        if (mExtensionFilter == null) {
            mExtensionFilter = new DefaultExtensionFilter();
        }
    }

    @Override
    public WebResourceResponse load(Chain chain) {
        CacheRequest request = chain.getRequest();
        boolean isFilter = mExtensionFilter.isFilter(request.getExtension());
        SourceRequest sourceRequest = new SourceRequest(request.getUrl(), isFilter, request.getHeaders());
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        WebResourceResponse response = generateWebResourceResponse(resource, request.getMime());
        if (response != null) {
            return response;
        }
        return chain.process(request);
    }

    @Override
    public void destroy() {
        if (mExtensionFilter != null) {
            mExtensionFilter.clearExtension();
        }
    }
}

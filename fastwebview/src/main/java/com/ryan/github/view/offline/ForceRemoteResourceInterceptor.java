package com.ryan.github.view.offline;

import android.content.Context;
import android.text.TextUtils;

import com.ryan.github.view.CacheConfig;
import com.ryan.github.view.MimeTypeFilter;
import com.ryan.github.view.WebResource;
import com.ryan.github.view.loader.OkHttpResourceLoader;
import com.ryan.github.view.loader.ResourceLoader;
import com.ryan.github.view.loader.SourceRequest;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class ForceRemoteResourceInterceptor implements Destroyable, ResourceInterceptor {

    private ResourceLoader mResourceLoader;
    private MimeTypeFilter mMimeTypeFilter;

    ForceRemoteResourceInterceptor(Context context, CacheConfig cacheConfig) {
        mResourceLoader = new OkHttpResourceLoader(context);
        mMimeTypeFilter = cacheConfig != null ? cacheConfig.getFilter() : null;
    }

    @Override
    public WebResource load(Chain chain) {
        CacheRequest request = chain.getRequest();
        String mime = request.getMime();
        boolean isFilter = TextUtils.isEmpty(mime) || mMimeTypeFilter.isFilter(mime);
        SourceRequest sourceRequest = new SourceRequest(request.getUrl(), isFilter, request.getHeaders());
        sourceRequest.setUserAgent(request.getUserAgent());
        WebResource resource = mResourceLoader.getResource(sourceRequest);
        if (resource != null) {
            return resource;
        }
        return chain.process(request);
    }

    @Override
    public void destroy() {
        if (mMimeTypeFilter != null) {
            mMimeTypeFilter.clear();
        }
    }
}

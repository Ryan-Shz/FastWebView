package com.ryan.github.view.loader;

import android.content.Context;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.okhttp.OkHttpClientHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Ryan
 * at 2019/9/26
 */
public class OkHttpResourceLoader implements ResourceLoader {

    private Context mContext;

    public OkHttpResourceLoader(Context context) {
        mContext = context;
    }

    @Override
    public WebResource getResource(SourceRequest sourceRequest) {
        String url = sourceRequest.getUrl();
        boolean isCache = sourceRequest.isCacheable();
        OkHttpClient client = OkHttpClientHolder.get(mContext);
        CacheControl.Builder builder = new CacheControl.Builder();
        if (!isCache) {
            builder.noStore();
        }
        Request.Builder requestBuilder = new Request.Builder();
        Map<String, String> headers = sourceRequest.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            requestBuilder.headers(Headers.of(headers));
        }
        Request request = requestBuilder
                .url(url)
                .get()
                .cacheControl(builder.build())
                .build();
        Response response;
        try {
            WebResource remoteResource = new WebResource();
            response = client.newCall(request).execute();
            if (response.code() == 304) {
                remoteResource.setModified(false);
                return remoteResource;
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                InputStream is = responseBody.byteStream();
                remoteResource.setModified(true);
                remoteResource.setInputStream(is);
                remoteResource.setResponseHeaders(response.headers().toMultimap());
                return remoteResource;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

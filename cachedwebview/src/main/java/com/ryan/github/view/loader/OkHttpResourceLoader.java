package com.ryan.github.view.loader;

import android.content.Context;
import android.text.TextUtils;

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

    private static final String USER_AGENT = "User-Agent";
    private Context mContext;

    public OkHttpResourceLoader(Context context) {
        mContext = context;
    }

    @Override
    public WebResource getResource(SourceRequest sourceRequest) {
        String url = sourceRequest.getUrl();
        boolean isCacheByOkHttp = sourceRequest.isCacheable();
        OkHttpClient client = OkHttpClientHolder.get(mContext);
        CacheControl.Builder builder = new CacheControl.Builder();
        if (!isCacheByOkHttp) {
            builder.noStore();
        }
        String userAgent = sourceRequest.getUserAgent();
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = "Android";
        }
        Request.Builder requestBuilder = new Request.Builder()
                .removeHeader(USER_AGENT)
                .addHeader(USER_AGENT, userAgent);
        Map<String, String> headers = sourceRequest.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.removeHeader(entry.getKey());
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder
                .url(url)
                .cacheControl(builder.build())
                .get()
                .build();
        Response response;
        try {
            WebResource remoteResource = new WebResource();
            response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                InputStream is = responseBody.byteStream();
                remoteResource.setModified(response.code() != 304);
                remoteResource.setInputStream(is);
                remoteResource.setResponseHeaders(response.headers().toMultimap());
                remoteResource.setCache(!isCacheByOkHttp);
                return remoteResource;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

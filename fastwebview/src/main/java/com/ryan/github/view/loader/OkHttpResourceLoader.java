package com.ryan.github.view.loader;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.okhttp.OkHttpClientProvider;
import com.ryan.github.view.utils.HeaderUtils;
import com.ryan.github.view.utils.LogUtils;
import com.ryan.github.view.ReusableInputStream;
import com.ryan.github.view.webview.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * load remote resources using okhttp.
 * <p>
 * Created by Ryan
 * at 2019/9/26
 */
public class OkHttpResourceLoader implements ResourceLoader {

    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String DEFAULT_USER_AGENT = "FastWebView" + BuildConfig.VERSION_NAME;
    private Context mContext;

    public OkHttpResourceLoader(Context context) {
        mContext = context;
    }

    @Override
    public WebResource getResource(SourceRequest sourceRequest) {
        String url = sourceRequest.getUrl();
        LogUtils.d(String.format("load url: %s", url));
        boolean isCacheByOkHttp = sourceRequest.isCacheable();
        OkHttpClient client = OkHttpClientProvider.get(mContext);
        CacheControl cacheControl;
        CacheControl.Builder builder = new CacheControl.Builder();
        if (isCacheByOkHttp) {
            cacheControl = getCacheControl(sourceRequest.getWebViewCache());
        } else {
            cacheControl = builder.noStore().build();
        }
        String userAgent = sourceRequest.getUserAgent();
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = DEFAULT_USER_AGENT;
        }
        Request.Builder requestBuilder = new Request.Builder()
                .removeHeader(HEADER_USER_AGENT)
                .addHeader(HEADER_USER_AGENT, userAgent)
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .addHeader("X-Requested-With", mContext.getPackageName())
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        Map<String, String> headers = sourceRequest.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.removeHeader(entry.getKey());
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder
                .url(url)
                .cacheControl(cacheControl)
                .get()
                .build();
        Response response;
        try {
            WebResource remoteResource = new WebResource();
            response = client.newCall(request).execute();
            if (response.code() == 200 || response.code() == 304) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream is = responseBody.byteStream();
                    remoteResource.setResponseCode(response.code());
                    remoteResource.setReasonPurase(response.message());
                    remoteResource.setModified(response.code() != 304);
                    ReusableInputStream inputStream = new ReusableInputStream(is);
                    remoteResource.setInputStream(inputStream);
                    remoteResource.setResponseHeaders(HeaderUtils.generateHeadersMap(response.headers()));
                    remoteResource.setCache(!isCacheByOkHttp);
                    return remoteResource;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CacheControl getCacheControl(int webViewCacheMode) {
        switch (webViewCacheMode) {
            case WebSettings.LOAD_CACHE_ONLY:
                return CacheControl.FORCE_CACHE;
            case WebSettings.LOAD_NO_CACHE:
                return CacheControl.FORCE_NETWORK;
            case WebSettings.LOAD_CACHE_ELSE_NETWORK:
                CacheControl.Builder builder = new CacheControl.Builder();
                builder.maxStale(Integer.MAX_VALUE, TimeUnit.DAYS);
                return builder.build();
            default:
                return new CacheControl.Builder().build();
        }
    }
}

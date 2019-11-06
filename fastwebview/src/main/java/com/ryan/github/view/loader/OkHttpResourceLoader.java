package com.ryan.github.view.loader;

import android.content.Context;
import android.text.TextUtils;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.okhttp.OkHttpClientProvider;
import com.ryan.github.view.utils.HeaderUtils;
import com.ryan.github.view.utils.LogUtils;
import com.ryan.github.view.utils.ReusableInputStream;
import com.ryan.github.view.webview.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;
import static android.webkit.WebSettings.LOAD_CACHE_ONLY;
import static android.webkit.WebSettings.LOAD_NO_CACHE;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_OK;

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
        CacheControl cacheControl = getCacheControl(sourceRequest.getWebViewCache(), isCacheByOkHttp);
        String userAgent = sourceRequest.getUserAgent();
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = DEFAULT_USER_AGENT;
        }
        Locale locale = Locale.getDefault();
        String acceptLanguage;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            acceptLanguage = locale.toLanguageTag();
        } else {
            acceptLanguage = locale.getLanguage();
        }
        if (!acceptLanguage.equalsIgnoreCase("en-US")) {
            acceptLanguage += ",en-US;q=0.9";
        }
        Request.Builder requestBuilder = new Request.Builder()
                .removeHeader(HEADER_USER_AGENT)
                .addHeader(HEADER_USER_AGENT, userAgent)
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("X-Requested-With", mContext.getPackageName())
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Language", acceptLanguage);
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
            if (response.code() == HTTP_OK || response.code() == HTTP_NOT_MODIFIED) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream is = responseBody.byteStream();
                    remoteResource.setResponseCode(response.code());
                    remoteResource.setReasonPhrase(response.message());
                    remoteResource.setModified(response.code() != HTTP_NOT_MODIFIED);
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

    private CacheControl getCacheControl(int webViewCacheMode, boolean isCacheByOkHttp) {
        // return the appropriate cache-control according to webview cache mode.
        switch (webViewCacheMode) {
            case LOAD_CACHE_ONLY:
                return CacheControl.FORCE_CACHE;
            case LOAD_CACHE_ELSE_NETWORK:
                if (!isCacheByOkHttp) {
                    // if it happens, because there is no local cache.
                    return createNoStoreCacheControl();
                }
                // tell okhttp that we are willing to receive expired cache.
                return new CacheControl.Builder().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
            case LOAD_NO_CACHE:
                return CacheControl.FORCE_NETWORK;
            default: // LOAD_DEFAULT
                return isCacheByOkHttp ? new CacheControl.Builder().build() : createNoStoreCacheControl();
        }
    }

    private CacheControl createNoStoreCacheControl() {
        return new CacheControl.Builder().noStore().build();
    }
}

package com.ryan.github.view.okhttp;

import android.content.Context;

import com.ryan.github.view.cookie.CookieConfigManager;
import com.ryan.github.view.cookie.CookieJarImpl;
import com.ryan.github.view.cookie.CookieStore;
import com.ryan.github.view.cookie.CookieStrategy;
import com.ryan.github.view.cookie.MemoryCookieStore;
import com.ryan.github.view.cookie.PersistentCookieStore;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Ryan
 * at 2019/9/26
 */
public class OkHttpClientProvider {

    private static final String CACHE_OKHTTP_DIR_NAME = "cached_webview_okhttp";
    private static final int OKHTTP_CACHE_SIZE = 100 * 1024 * 1024;
    private OkHttpClient mClient;

    private OkHttpClientProvider() {

    }

    private void ensureOkHttpClientCreated(Context context) {
        if (mClient == null) {
            CookieStore cookieStore = getCookieStore(context);
            String dir = context.getCacheDir() + File.separator + CACHE_OKHTTP_DIR_NAME;
            mClient = new OkHttpClient.Builder()
                    .cookieJar(new CookieJarImpl(cookieStore))
                    .cache(new Cache(new File(dir), OKHTTP_CACHE_SIZE))
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
    }

    private static class SingletonHolder {
        private static final OkHttpClientProvider INSTANCE = new OkHttpClientProvider();
    }

    public static OkHttpClient get(Context context) {
        SingletonHolder.INSTANCE.ensureOkHttpClientCreated(context);
        return SingletonHolder.INSTANCE.mClient;
    }

    private CookieStore getCookieStore(Context context) {
        CookieStore cookieStore;
        if (CookieConfigManager.getInstance().getCookieStrategy() == CookieStrategy.PERSISTENT) {
            cookieStore = new PersistentCookieStore(context);
        } else {
            cookieStore = new MemoryCookieStore();
        }
        return cookieStore;
    }

}

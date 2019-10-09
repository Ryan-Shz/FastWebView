package com.ryan.github.view.okhttp;

import android.content.Context;

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
    private OkHttpClient mClient;

    private OkHttpClientProvider() {

    }

    private void ensureOkHttpClientCreated(Context context) {
        if (mClient == null) {
            String dir = context.getCacheDir() + File.separator + CACHE_OKHTTP_DIR_NAME;
            mClient = new OkHttpClient.Builder()
                    .cache(new Cache(new File(dir), 100 * 1024 * 1024))
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

}

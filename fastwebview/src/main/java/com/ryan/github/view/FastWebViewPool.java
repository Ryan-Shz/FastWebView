package com.ryan.github.view;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.v4.util.Pools;
import android.util.Log;

import com.ryan.github.view.webview.BuildConfig;

/**
 * A simple webview instance pool.
 * Reduce webview initialization time about 100ms.
 * my test env: vivo-x23, android api: 8.1
 * <p>
 * Created by Ryan
 * at 2019/11/4
 */
public class FastWebViewPool {

    public static final String TAG = "FastWebViewPool";
    private static final int MAX_POOL_SIZE = 2;
    private static final Pools.Pool<FastWebView> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    public static void prepare(Context context) {
        release(acquire(context.getApplicationContext()));
    }

    public static FastWebView acquire(Context context) {
        FastWebView webView = sPool.acquire();
        if (webView == null) {
            MutableContextWrapper wrapper = new MutableContextWrapper(context);
            webView = new FastWebView(wrapper);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "create new webview instance.");
            }
        } else {
            MutableContextWrapper wrapper = (MutableContextWrapper) webView.getContext();
            wrapper.setBaseContext(context);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "obtain webview instance from pool.");
            }
        }
        return webView;
    }

    public static void release(FastWebView webView) {
        if (webView == null) {
            return;
        }
        webView.release();
        MutableContextWrapper wrapper = (MutableContextWrapper) webView.getContext();
        wrapper.setBaseContext(wrapper.getApplicationContext());
        sPool.release(webView);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "release webview instance to pool.");
        }
    }
}

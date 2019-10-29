package com.ryan.github.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryan.github.view.cookie.CookieInterceptor;
import com.ryan.github.view.cookie.CookieConfigManager;
import com.ryan.github.view.cookie.CookieStrategy;
import com.ryan.github.view.utils.NetworkUtils;
import com.ryan.github.view.offline.ResourceInterceptor;

import java.util.Map;

/**
 * Created by Ryan
 * 2018/2/7 下午3:33
 */
public class FastWebView extends WebView {

    private WebViewCache mWebViewCache;
    private CacheWebViewClient mCacheWebViewClient;
    private WebViewClient mUserWebViewClient;
    private CookieConfigManager mCookieManager;

    public FastWebView(Context context) {
        this(context, null);
    }

    public FastWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCookieManager = CookieConfigManager.getInstance();
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        if (mCacheWebViewClient != null) {
            mCacheWebViewClient.updateProxyClient(client);
        } else {
            super.setWebViewClient(client);
        }
        mUserWebViewClient = client;
    }

    @Override
    public void destroy() {
        stopLoading();
        getSettings().setJavaScriptEnabled(false);
        clearHistory();
        removeAllViews();
        ViewParent viewParent = this.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(this);
        }
        if (mWebViewCache != null) {
            mWebViewCache.destroyResource();
        }
        mCookieManager.clear();
        super.destroy();
    }

    private void initCache() {
        initCacheMode();
        setCacheConfig(null);
    }

    public void setCacheConfig(CacheConfig cacheConfig) {
        mCacheWebViewClient = new CacheWebViewClient();
        mWebViewCache = new WebViewCacheImpl(getContext(), cacheConfig);
        mCacheWebViewClient.setWebViewCache(mWebViewCache);
        if (mUserWebViewClient != null) {
            mCacheWebViewClient.updateProxyClient(mUserWebViewClient);
        }
        super.setWebViewClient(mCacheWebViewClient);
    }

    public void openForceCache() {
        initCache();
        mWebViewCache.openForceMode();
    }

    public void openDefaultCache() {
        initCache();
    }

    @Override
    public void loadUrl(String url) {
        loadUrl(url, null);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (mWebViewCache != null) {
            mWebViewCache.setUserAgent(getSettings().getUserAgentString());
            mWebViewCache.addHeader(url, additionalHttpHeaders);
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    public static void preload(Context context, String url) {
        new FastWebView(context.getApplicationContext()).loadUrl(url);
    }

    public void addResourceInterceptor(ResourceInterceptor interceptor) {
        mWebViewCache.addResourceInterceptor(interceptor);
    }

    public void runJs(String function, Object... args) {
        StringBuilder script = new StringBuilder("javascript:");
        script.append(function).append("(");
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (null == arg) {
                    continue;
                }
                if (arg instanceof String) {
                    arg = "'" + arg + "'";
                }
                script.append(arg);
                if (i != args.length - 1) {
                    script.append(",");
                }
            }
        }
        script.append(");");
        runJs(script.toString());
    }

    private void runJs(String script) {
        this.loadUrl(script);
    }

    private void initCacheMode() {
        WebSettings webSettings = getSettings();
        if (NetworkUtils.isAvailable(getContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public WebViewClient getWebViewClient() {
        return mUserWebViewClient != null ? mUserWebViewClient : super.getWebViewClient();
    }

    public void setCookieStrategy(CookieStrategy strategy) {
        mCookieManager.setCookieStrategy(strategy);
    }

    public void setRequestCookieInterceptor(CookieInterceptor interceptor) {
        mCookieManager.setRequestCookieInterceptor(interceptor);
    }

    public void setResponseCookieInterceptor(CookieInterceptor interceptor) {
        mCookieManager.setResponseCookieInterceptor(interceptor);
    }
}

package com.ryan.github.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryan.github.view.utils.NetworkUtils;
import com.ryan.github.view.offline.ResourceInterceptor;

import java.util.Map;

/**
 * Created by Ryan
 * 2018/2/7 下午3:33
 */
public class CachedWebView extends WebView {

    private WebViewCache mWebViewCache;
    private CacheWebViewClient mCacheWebViewClient;

    public CachedWebView(Context context) {
        this(context, null);
    }

    public CachedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CachedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCacheMode();
        setCacheConfig(null);
    }

    private void setCacheMode() {
        WebSettings webSettings = getSettings();
        if (NetworkUtils.isAvailable(getContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        mCacheWebViewClient.updateProxyClient(client);
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
        super.destroy();
    }

    public void setCacheConfig(CacheConfig cacheConfig) {
        mCacheWebViewClient = new CacheWebViewClient();
        mWebViewCache = new WebViewCacheImpl(getContext(), cacheConfig);
        mCacheWebViewClient.setWebViewCache(mWebViewCache);
        super.setWebViewClient(mCacheWebViewClient);
    }

    public void forceCache() {
        mWebViewCache.openForceMode();
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
        new CachedWebView(context.getApplicationContext()).loadUrl(url);
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

}

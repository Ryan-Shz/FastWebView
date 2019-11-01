package com.ryan.github.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryan.github.view.config.CacheConfig;
import com.ryan.github.view.config.FastCacheMode;
import com.ryan.github.view.cookie.CookieInterceptor;
import com.ryan.github.view.cookie.CookieConfigManager;
import com.ryan.github.view.cookie.CookieStrategy;
import com.ryan.github.view.offline.ResourceInterceptor;

/**
 * Created by Ryan
 * 2018/2/7 下午3:33
 */
public class FastWebView extends WebView implements FastOpenApi {

    private InnerFastClient mFastClient;
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
        if (mFastClient != null) {
            mFastClient.updateProxyClient(client);
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
        if (mFastClient != null) {
            mFastClient.destroy();
        }
        mCookieManager.clear();
        super.destroy();
    }

    public static void preload(Context context, String url) {
        new FastWebView(context.getApplicationContext()).loadUrl(url);
    }

    public void setCacheMode(FastCacheMode mode) {
        setCacheMode(mode, null);
    }

    @Override
    public void setCacheMode(FastCacheMode mode, CacheConfig cacheConfig) {
        if (mode == FastCacheMode.DEFAULT) {
            mFastClient = null;
            if (mUserWebViewClient != null) {
                setWebViewClient(mUserWebViewClient);
            }
        } else {
            mFastClient = new InnerFastClient(this);
            if (mUserWebViewClient != null) {
                mFastClient.updateProxyClient(mUserWebViewClient);
            }
            mFastClient.setCacheMode(mode, cacheConfig);
            super.setWebViewClient(mFastClient);
        }
    }

    public void addResourceInterceptor(ResourceInterceptor interceptor) {
        if (mFastClient != null) {
            mFastClient.addResourceInterceptor(interceptor);
        }
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

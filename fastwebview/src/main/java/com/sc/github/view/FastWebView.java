package com.sc.github.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sc.github.view.utils.NetworkUtils;

import java.util.Map;

/**
 * @author shamschu
 * @Date 2018/2/7 下午3:33
 */
public class FastWebView extends WebView {

    private IWebViewCache mWebViewCache;
    private WebViewClient mOriginalClient;
    private CacheWebViewClient mCacheWebViewClient;
    private boolean mEnableDiskCache = false;

    public FastWebView(Context context) {
        this(context, null);
    }

    public FastWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        setCacheMode();
    }

    private void setCacheMode() {
        WebSettings webSettings = this.getSettings();
        if (NetworkUtils.isAvailable(this.getContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        mOriginalClient = client;
        setDiskCacheEnable(mEnableDiskCache);
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

    public DiskCacheConfig generateDiskCacheConfig() {
        return null;
    }

    public void setDiskCacheEnable(boolean enable) {
        mEnableDiskCache = enable;
        if (!mEnableDiskCache) {
            if (mOriginalClient != null) {
                super.setWebViewClient(mOriginalClient);
            }
            if (mWebViewCache != null) {
                mWebViewCache.destroyResource();
                mWebViewCache = null;
            }
            mCacheWebViewClient = null;
            return;
        }
        if (mCacheWebViewClient == null) {
            mCacheWebViewClient = new CacheWebViewClient();
            mWebViewCache = new WebViewCache(getContext(), generateDiskCacheConfig());
            mCacheWebViewClient.setWebViewCache(mWebViewCache);
            super.setWebViewClient(mCacheWebViewClient);
        }
        mCacheWebViewClient.updateWebViewClient(mOriginalClient);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (mWebViewCache != null && mWebViewCache instanceof IRemoteRequestConfig) {
            IRemoteRequestConfig requestConfig = (IRemoteRequestConfig) mWebViewCache;
            requestConfig.addHeader(url, additionalHttpHeaders);
            requestConfig.setOriginalUrl(url);
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    public static void preload(Context context, String url) {
        new FastWebView(context.getApplicationContext()).loadUrl(url);
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

    public IWebViewCache getWebViewCache() {
        return mWebViewCache;
    }
}

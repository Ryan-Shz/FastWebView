package com.ryan.github.webview.sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;

import com.ryan.github.view.CachedWebView;
import com.ryan.github.view.offline.Chain;
import com.ryan.github.view.offline.ResourceInterceptor;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CachedWebView cachedWebView = findViewById(R.id.fast_web_view);
        cachedWebView.setFocusable(true);
        cachedWebView.setFocusableInTouchMode(true);
        WebSettings webSettings = cachedWebView.getSettings();
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
            cookieManager.setAcceptThirdPartyCookies(cachedWebView, true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        String url = "https://github.com/Ryan-Shz";
        cachedWebView.forceCache();
        cachedWebView.addResourceInterceptor(new ResourceInterceptor() {
            @Override
            public WebResourceResponse load(Chain chain) {
                return chain.process(chain.getRequest());
            }
        });
        cachedWebView.loadUrl(url);
    }
}



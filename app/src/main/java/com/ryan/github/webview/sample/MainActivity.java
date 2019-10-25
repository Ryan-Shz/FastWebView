package com.ryan.github.webview.sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.ryan.github.view.FastWebView;
import com.ryan.github.view.WebResource;
import com.ryan.github.view.offline.Chain;
import com.ryan.github.view.offline.ResourceInterceptor;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FastWebView";

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FastWebView fastWebView = new FastWebView(this);
        setContentView(fastWebView);
        fastWebView.setFocusable(true);
        fastWebView.setFocusableInTouchMode(true);
        final WebSettings webSettings = fastWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBlockNetworkImage(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(fastWebView, true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        fastWebView.openForceCache();
        fastWebView.addResourceInterceptor(new ResourceInterceptor() {
            @Override
            public WebResource load(Chain chain) {
                return chain.process(chain.getRequest());
            }
        });
        fastWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webSettings.setBlockNetworkImage(false);
                view.loadUrl("javascript:android.sendResource(JSON.stringify(window.performance.timing))");
            }
        });
        fastWebView.addJavascriptInterface(this, "android");
        Map<String, String> headers = new HashMap<>();
        headers.put("custom", "test");
        fastWebView.loadUrl("https://github.com/Ryan-Shz", headers);
    }

    @JavascriptInterface
    public void sendResource(String timing){
        Performance performance = new Gson().fromJson(timing, Performance.class);
        Log.v(TAG, "request cost time: " + (performance.getResponseEnd() - performance.getRequestStart()) + "ms");
        Log.v(TAG, "dom build time: " + (performance.getDomComplete() - performance.getDomInteractive()) + "ms.");
        Log.v(TAG, "dom ready time: " + (performance.getDomContentLoadedEventEnd() - performance.getNavigationStart()) + "ms.");
        Log.v(TAG, "load time: " + (performance.getLoadEventEnd() - performance.getNavigationStart()) + "ms.");
        Log.v(TAG, "===================");
    }
}



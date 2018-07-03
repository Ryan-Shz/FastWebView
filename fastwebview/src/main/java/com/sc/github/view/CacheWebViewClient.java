package com.sc.github.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 实现缓存拦截
 * 完全代理外部使用的WebViewClient对象
 *
 * @author shamschu
 * @Date 2018/2/7 下午3:39
 */
class CacheWebViewClient extends WebViewClient {

    private WebViewClient mWebViewClient;
    private IWebViewCache mWebViewCache;

    void updateWebViewClient(WebViewClient webViewClient) {
        mWebViewClient = webViewClient;
    }

    void setWebViewCache(IWebViewCache webViewCache) {
        this.mWebViewCache = webViewCache;
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (mWebViewClient != null) {
            mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
            return;
        }
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedHttpError(view, request, errorResponse);
            return;
        }
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (mWebViewClient != null) {
            mWebViewClient.onFormResubmission(view, dontResend, resend);
            return;
        }
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (mWebViewClient != null) {
            mWebViewClient.doUpdateVisitedHistory(view, url, isReload);
            return;
        }
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedSslError(view, handler, error);
            return;
        }
        super.onReceivedSslError(view, handler, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedClientCertRequest(view, request);
            return;
        }
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
            return;
        }
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldOverrideKeyEvent(view, event);
        }
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (mWebViewClient != null) {
            mWebViewClient.onUnhandledKeyEvent(view, event);
            return;
        }
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (mWebViewClient != null) {
            mWebViewClient.onScaleChanged(view, oldScale, newScale);
            return;
        }
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
            return;
        }
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        if (mWebViewClient != null) {
            return mWebViewClient.onRenderProcessGone(view, detail);
        }
        return super.onRenderProcessGone(view, detail);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
            return;
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (mWebViewClient != null) {
            mWebViewClient.onReceivedError(view, request, error);
            return;
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (mWebViewClient != null) {
            mWebViewClient.onPageStarted(view, url, favicon);
            return;
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mWebViewClient != null) {
            mWebViewClient.onPageFinished(view, url);
            return;
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (mWebViewClient != null) {
            mWebViewClient.onLoadResource(view, url);
            return;
        }
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        if (mWebViewClient != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebViewClient.onPageCommitVisible(view, url);
            return;
        }
        super.onPageCommitVisible(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldOverrideUrlLoading(view, url);
        }
        view.loadUrl(url);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (mWebViewClient != null) {
            return mWebViewClient.shouldOverrideUrlLoading(view, request);
        }
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return onIntercept(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return onIntercept(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse onIntercept(WebView view, WebResourceRequest request) {
        if (mWebViewClient != null) {
            WebResourceResponse response = mWebViewClient.shouldInterceptRequest(view, request);
            if (response != null) {
                return response;
            }
        }
        return getResourceFromCache(request.getUrl().toString());
    }

    private WebResourceResponse onIntercept(WebView view, String url) {
        if (mWebViewClient != null) {
            WebResourceResponse response = mWebViewClient.shouldInterceptRequest(view, url);
            if (response != null) {
                return response;
            }
        }
        return getResourceFromCache(url);
    }

    private WebResourceResponse getResourceFromCache(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return mWebViewCache.getResource(url);
    }

}

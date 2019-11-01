package com.ryan.github.view;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.offline.Destroyable;

/**
 * Created by Ryan
 * 2018/2/7 下午5:06
 */
public interface WebViewCache extends FastOpenApi, Destroyable {

    WebResourceResponse getResource(WebResourceRequest request, int webViewCacheMode, String userAgent);

}

package com.sc.github.view;

import android.webkit.WebResourceResponse;

/**
 * WebView缓存职责
 *
 * @author shamschu
 * @Date 2018/2/7 下午5:06
 */
public interface IWebViewCache {

    /**
     * 获取指定Url的缓存资源
     *
     * @param url 指定的url
     * @return 缓存资源 {@link WebResourceResponse}
     */
    WebResourceResponse getResource(String url);

    /**
     * 资源释放
     */
    void destroyResource();

}

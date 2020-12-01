package com.ryan.github.view.cookie;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.CookieManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class CookieJarImpl implements CookieJar {

    private FastCookieManager mCookieManager;

    public CookieJarImpl() {
        mCookieManager = FastCookieManager.getInstance();
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        List<CookieInterceptor> interceptors = mCookieManager.getRequestCookieInterceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CookieInterceptor interceptor : interceptors) {
                cookies = interceptor.newCookies(url, cookies);
            }
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        for (Cookie cookie : cookies) {
            cookieManager.setCookie(url.toString(), cookie.toString());
        }
    }

    @NonNull
    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        String cookieFullStr = CookieManager.getInstance().getCookie(url.host());
        if (!TextUtils.isEmpty(cookieFullStr)) {
            String[] cookieArr = cookieFullStr.split(";");
            for (String cookieStr : cookieArr) {
                Cookie cookie = Cookie.parse(url, cookieStr);
                if (cookie != null) {
                    cookies.add(cookie);
                }
            }
        }
        List<CookieInterceptor> interceptors = mCookieManager.getResponseCookieInterceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CookieInterceptor interceptor : interceptors) {
                cookies = interceptor.newCookies(url, cookies);
            }
        }
        return cookies;
    }
}

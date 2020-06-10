package com.ryan.github.view.cookie;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.CookieManager;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class CookieJarImpl implements CookieJar {

    private CookieStore mCookieStore;
    private FastCookieManager mCookieManager;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null.");
        }
        mCookieManager = FastCookieManager.getInstance();
        mCookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        CookieInterceptor interceptor = mCookieManager.getResponseCookieInterceptor();
        if (interceptor != null) {
            cookies = interceptor.newCookies(url, cookies);
        }
        mCookieStore.add(url, cookies);
    }

    @NonNull
    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = mCookieStore.get(url);
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
        CookieInterceptor interceptor = mCookieManager.getRequestCookieInterceptor();
        if (interceptor != null) {
            cookies = interceptor.newCookies(url, cookies);
        }
        return cookies;
    }
}

package com.ryan.github.view.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public interface CookieInterceptor {

    List<Cookie> newCookies(HttpUrl url, List<Cookie> originCookies);

}

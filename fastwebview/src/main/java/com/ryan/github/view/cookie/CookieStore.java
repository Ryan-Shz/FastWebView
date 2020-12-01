package com.ryan.github.view.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
@Deprecated
public interface CookieStore {

    void add(HttpUrl httpUrl, Cookie cookie);

    void add(HttpUrl httpUrl, List<Cookie> cookies);

    List<Cookie> get(HttpUrl httpUrl);

    List<Cookie> getCookies();

    boolean remove(HttpUrl httpUrl, Cookie cookie);

    boolean removeAll();
}

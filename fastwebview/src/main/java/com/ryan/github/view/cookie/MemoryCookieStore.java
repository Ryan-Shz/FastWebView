package com.ryan.github.view.cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class MemoryCookieStore implements CookieStore {

    private static final String HOST_NAME_PREFIX = "host_";

    private final HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;

    MemoryCookieStore() {
        this.cookies = new HashMap<>();
    }

    @Override
    public void add(HttpUrl httpUrl, Cookie cookie) {
        if (!cookie.persistent()) {
            return;
        }
        String name = this.cookieName(cookie);
        String hostKey = this.hostName(httpUrl);
        if (!this.cookies.containsKey(hostKey)) {
            this.cookies.put(hostKey, new ConcurrentHashMap<String, Cookie>());
        }
        cookies.get(hostKey).put(name, cookie);
    }

    @Override
    public void add(HttpUrl httpUrl, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (isCookieExpired(cookie)) {
                continue;
            }
            this.add(httpUrl, cookie);
        }
    }

    @Override
    public List<Cookie> get(HttpUrl httpUrl) {
        return this.get(this.hostName(httpUrl));
    }

    @Override
    public List<Cookie> getCookies() {
        ArrayList<Cookie> result = new ArrayList<>();
        for (String hostKey : this.cookies.keySet()) {
            result.addAll(this.get(hostKey));
        }
        return result;
    }

    private List<Cookie> get(String hostKey) {
        ArrayList<Cookie> result = new ArrayList<>();
        if (this.cookies.containsKey(hostKey)) {
            Collection<Cookie> cookies = this.cookies.get(hostKey).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    this.remove(hostKey, cookie);
                } else {
                    result.add(cookie);
                }
            }
        }
        return result;
    }

    @Override
    public boolean remove(HttpUrl httpUrl, Cookie cookie) {
        return this.remove(this.hostName(httpUrl), cookie);
    }

    private boolean remove(String hostKey, Cookie cookie) {
        String name = this.cookieName(cookie);
        if (this.cookies.containsKey(hostKey) && this.cookies.get(hostKey).containsKey(name)) {
            this.cookies.get(hostKey).remove(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll() {
        this.cookies.clear();
        return true;
    }

    private boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    private String hostName(HttpUrl httpUrl) {
        return httpUrl.host().startsWith(HOST_NAME_PREFIX) ? httpUrl.host() : HOST_NAME_PREFIX + httpUrl.host();
    }

    private String cookieName(Cookie cookie) {
        return cookie == null ? null : cookie.name() + cookie.domain();
    }
}

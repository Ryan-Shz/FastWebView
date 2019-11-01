package com.ryan.github.view.cookie;

import android.content.Context;

import com.ryan.github.view.offline.Destroyable;

import okhttp3.CookieJar;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class FastCookieManager implements Destroyable {

    private CookieInterceptor requestCookieInterceptor;
    private CookieInterceptor responseCookieInterceptor;
    private CookieStrategy cookieStrategy;
    private CookieJar mUserCookieJar;

    private FastCookieManager() {

    }

    @Override
    public void destroy() {
        requestCookieInterceptor = null;
        responseCookieInterceptor = null;
        cookieStrategy = null;
        mUserCookieJar = null;
    }

    private static class SingletonHolder {
        private static final FastCookieManager INSTANCE = new FastCookieManager();
    }

    public static FastCookieManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    CookieInterceptor getRequestCookieInterceptor() {
        return requestCookieInterceptor;
    }

    public void setRequestCookieInterceptor(CookieInterceptor requestCookieInterceptor) {
        this.requestCookieInterceptor = requestCookieInterceptor;
    }

    CookieInterceptor getResponseCookieInterceptor() {
        return responseCookieInterceptor;
    }

    public void setResponseCookieInterceptor(CookieInterceptor responseCookieInterceptor) {
        this.responseCookieInterceptor = responseCookieInterceptor;
    }

    public void setCookieStrategy(CookieStrategy cookieStrategy) {
        this.cookieStrategy = cookieStrategy;
    }

    public CookieStrategy getCookieStrategy() {
        return cookieStrategy;
    }

    public CookieJar getCookieJar(Context context) {
        return mUserCookieJar != null ? mUserCookieJar : new CookieJarImpl(getCookieStore(context));
    }

    public void setCookieJar(CookieJar cookieJar) {
        this.mUserCookieJar = cookieJar;
    }

    private CookieStore getCookieStore(Context context) {
        CookieStore cookieStore;
        if (getCookieStrategy() == CookieStrategy.PERSISTENT) {
            cookieStore = new PersistentCookieStore(context);
        } else {
            cookieStore = new MemoryCookieStore();
        }
        return cookieStore;
    }
}

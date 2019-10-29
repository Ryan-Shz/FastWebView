package com.ryan.github.view.cookie;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class CookieConfigManager {

    private CookieInterceptor requestCookieInterceptor;
    private CookieInterceptor responseCookieInterceptor;
    private CookieStrategy cookieStrategy;

    private CookieConfigManager() {

    }

    private static class SingletonHolder {
        private static final CookieConfigManager INSTANCE = new CookieConfigManager();
    }

    public static CookieConfigManager getInstance() {
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

    public void clear() {
        requestCookieInterceptor = null;
        responseCookieInterceptor = null;
        cookieStrategy = null;
    }

    public void setCookieStrategy(CookieStrategy cookieStrategy) {
        this.cookieStrategy = cookieStrategy;
    }

    public CookieStrategy getCookieStrategy() {
        return cookieStrategy;
    }
}

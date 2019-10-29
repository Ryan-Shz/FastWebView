package com.ryan.github.view.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Ryan
 * on 2019/10/29
 */
public class PersistentCookieStore implements CookieStore {

    private static final String LOG_TAG = PersistentCookieStore.class.getSimpleName();
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String HOST_NAME_PREFIX = "host_";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final Map<String, Map<String, Cookie>> mCookies;
    private final SharedPreferences mCookiePrefs;
    private boolean mOmitNonPersistentCookies = false;

    public PersistentCookieStore(Context context) {
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        mCookies = new ConcurrentHashMap<>();
        // load persistent cookie from disk.
        loadPersistentCookie();
        // try clear expired cookie.
        clearExpired();
    }

    private void loadPersistentCookie() {
        Map<String, ?> tempCookieMap = mCookiePrefs.getAll();
        for (String key : tempCookieMap.keySet()) {
            if (key == null || !key.contains(HOST_NAME_PREFIX)) {
                continue;
            }
            String cookieNames = String.valueOf(tempCookieMap.get(key));
            if (TextUtils.isEmpty(cookieNames)) {
                continue;
            }
            if (!mCookies.containsKey(key)) {
                mCookies.put(key, new ConcurrentHashMap<String, Cookie>());
            }
            String[] cookieNameArr = cookieNames.split(",");
            for (String name : cookieNameArr) {
                String encodedCookie = mCookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                if (encodedCookie == null) {
                    continue;
                }
                Cookie decodedCookie = this.decodeCookie(encodedCookie);
                if (decodedCookie != null) {
                    mCookies.get(key).put(name, decodedCookie);
                }
            }
        }
        tempCookieMap.clear();
    }

    private void clearExpired() {
        SharedPreferences.Editor prefsEditor = mCookiePrefs.edit();
        for (String key : mCookies.keySet()) {
            boolean changeFlag = false;
            Map<String, Cookie> singleKeyCookies = mCookies.get(key);
            for (Map.Entry<String, Cookie> entry : singleKeyCookies.entrySet()) {
                String name = entry.getKey();
                Cookie cookie = entry.getValue();
                if (isCookieExpired(cookie)) {
                    // Clear cookie from local store
                    singleKeyCookies.remove(name);
                    // Clear cookie from persistent store
                    prefsEditor.remove(COOKIE_NAME_PREFIX + name);
                    changeFlag = true;
                }
            }
            // Update names in persistent store
            if (changeFlag) {
                prefsEditor.putString(key, TextUtils.join(",", mCookies.keySet()));
            }
        }
        prefsEditor.apply();
    }

    @Override
    public void add(HttpUrl httpUrl, Cookie cookie) {
        if (mOmitNonPersistentCookies && !cookie.persistent()) {
            return;
        }
        String name = cookieName(cookie);
        String hostName = hostName(httpUrl);
        // save cookie into local store, or remove if expired
        Map<String, Cookie> hostCookiesMap;
        if (mCookies.containsKey(hostName)) {
            hostCookiesMap = mCookies.get(hostName);
        } else {
            hostCookiesMap = new ConcurrentHashMap<>();
            mCookies.put(hostName, hostCookiesMap);
        }
        hostCookiesMap.put(name, cookie);
        // save cookie into persistent store
        SharedPreferences.Editor prefsEditor = mCookiePrefs.edit();
        prefsEditor.putString(hostName, TextUtils.join(",", hostCookiesMap.keySet()));
        prefsEditor.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
        prefsEditor.apply();
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
        for (String hostKey : mCookies.keySet()) {
            result.addAll(this.get(hostKey));
        }
        return result;
    }

    private List<Cookie> get(String hostKey) {
        List<Cookie> result = new ArrayList<>();
        if (mCookies.containsKey(hostKey)) {
            Collection<Cookie> cookies = mCookies.get(hostKey).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    remove(hostKey, cookie);
                } else {
                    result.add(cookie);
                }
            }
        }
        return result;
    }

    @Override
    public boolean remove(HttpUrl httpUrl, Cookie cookie) {
        return remove(hostName(httpUrl), cookie);
    }

    private boolean remove(String hostKey, Cookie cookie) {
        String name = this.cookieName(cookie);
        if (mCookies.containsKey(hostKey) && mCookies.get(hostKey).containsKey(name)) {
            mCookies.get(hostKey).remove(name);
            SharedPreferences.Editor prefsEditor = mCookiePrefs.edit();
            prefsEditor.remove(COOKIE_NAME_PREFIX + name);
            prefsEditor.putString(hostKey, TextUtils.join(",", mCookies.get(hostKey).keySet()));
            prefsEditor.apply();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll() {
        SharedPreferences.Editor prefsEditor = mCookiePrefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();
        mCookies.clear();
        return true;
    }

    public void setOmitNonPersistentCookies(boolean omitNonPersistentCookies) {
        this.mOmitNonPersistentCookies = omitNonPersistentCookies;
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

    private String encodeCookie(SerializableCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e);
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    private Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }

    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}

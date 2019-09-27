package com.ryan.github.view.loader;

import android.util.Log;

import com.ryan.github.view.WebResource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 远程资源加载
 * 使用HttpUrlConnection下载静态资源
 * 如下载请求的是初始网址，或获取返回头部
 * <p>
 * Created by Ryan
 * 2018/2/7 下午7:55
 */
public class DefaultResourceLoader implements ResourceLoader {

    private static final String TAG = DefaultResourceLoader.class.getName();

    @Override
    public WebResource getResource(SourceRequest sourceRequest) {
        String url = sourceRequest.getUrl();
        try {
            URL urlRequest = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlRequest.openConnection();
            putHeader(httpURLConnection, sourceRequest.getHeaders());
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                WebResource remoteResource = new WebResource();
                remoteResource.setInputStream(httpURLConnection.getInputStream());
                remoteResource.setResponseHeaders(httpURLConnection.getHeaderFields());
                return remoteResource;
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.d(TAG, e.toString() + " " + url);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, e.toString() + " " + url);
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, e.toString() + " " + url);
            e.printStackTrace();
        }
        return null;
    }

    private void putHeader(HttpURLConnection httpURLConnection, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

}

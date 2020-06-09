package com.ryan.github.view.offline;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.WebResource;
import com.ryan.github.view.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Ryan
 * at 2019/10/8
 */
public class DefaultWebResponseGenerator implements WebResourceResponseGenerator {

    @Override
    public WebResourceResponse generate(WebResource resource, String urlMime) {
        if (resource == null) {
            return null;
        }
        Map<String, String> headers = resource.getResponseHeaders();
        String contentType = null;
        String charset = null;
        if (headers != null) {
            String contentTypeKey = "Content-Type";
            if (headers.containsKey(contentTypeKey)) {
                String contentTypeValue = headers.get(contentTypeKey);
                if (!TextUtils.isEmpty(contentTypeValue)) {
                    String[] contentTypeArray = contentTypeValue.split(";");
                    if (contentTypeArray.length >= 1) {
                        contentType = contentTypeArray[0];
                    }
                    if (contentTypeArray.length >= 2) {
                        charset = contentTypeArray[1];
                        String[] charsetArray = charset.split("=");
                        if (charsetArray.length >= 2) {
                            charset = charsetArray[1];
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(contentType)) {
            urlMime = contentType;
        }
        if (TextUtils.isEmpty(urlMime)) {
            return null;
        }
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        byte[] resourceBytes = resource.getOriginBytes();
        if (resourceBytes == null || resourceBytes.length < 0) {
            return null;
        }
        if (resourceBytes.length == 0 && resource.getResponseCode() == 304) {
            LogUtils.e("the response bytes can not be empty if we get 304.");
            return null;
        }
        InputStream bis = new ByteArrayInputStream(resourceBytes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int status = resource.getResponseCode();
            String reasonPhrase = resource.getReasonPhrase();
            if (TextUtils.isEmpty(reasonPhrase) && status == 200) {
                reasonPhrase = "OK";
            }
            return new WebResourceResponse(urlMime, charset, status, reasonPhrase, resource.getResponseHeaders(), bis);
        }
        return new WebResourceResponse(urlMime, charset, bis);
    }
}

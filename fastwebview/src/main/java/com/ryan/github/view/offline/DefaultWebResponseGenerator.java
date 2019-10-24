package com.ryan.github.view.offline;

import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.ryan.github.view.WebResource;

import java.io.InputStream;
import java.util.List;
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
        Map<String, List<String>> headers = resource.getResponseHeaders();
        String contentType = null;
        String charset = null;
        if (headers != null) {
            String contentTypeKey = "content-type";
            if (headers.containsKey(contentTypeKey)) {
                List<String> contentTypeList = headers.get(contentTypeKey);
                if (contentTypeList != null && !contentTypeList.isEmpty()) {
                    String rawContentType = contentTypeList.get(0);
                    String[] contentTypeArray = rawContentType.split(";");
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
        if (TextUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        return createWebResourceResponse(resource.getInputStream(), urlMime, charset);
    }

    private WebResourceResponse createWebResourceResponse(InputStream inputStream, String mimeType, String charset) {
        try {
            if (inputStream != null) {
                return new WebResourceResponse(mimeType, charset, inputStream);
            }
        } catch (Exception ignore) {
        }
        return null;
    }
}

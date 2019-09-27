package com.ryan.github.view.offline;

import android.webkit.WebResourceResponse;

import com.ryan.github.view.utils.InputStreamUtils;
import com.ryan.github.view.WebResource;

import java.io.InputStream;

/**
 * Created by Ryan
 * at 2019/9/27
 */
abstract class BaseResourceInterceptor implements ResourceInterceptor {

    WebResourceResponse createWebResourceResponse(InputStream inputStream, String mimeType) {
        try {
            if (inputStream != null) {
                return new WebResourceResponse(mimeType, "UTF-8", inputStream);
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    WebResourceResponse generateWebResourceResponse(WebResource resource, String mimeType) {
        if (resource == null) {
            return null;
        }
        InputStream inputStream = InputStreamUtils.copy(resource.getInputStream());
        return createWebResourceResponse(inputStream, mimeType);
    }

}

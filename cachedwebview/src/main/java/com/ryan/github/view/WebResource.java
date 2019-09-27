package com.ryan.github.view;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Ryan
 * 2018/3/1 下午5:40
 */
public class WebResource {

    // 资源输入流
    private InputStream inputStream;

    // 服务端相应头部信息
    private Map<String, List<String>> responseHeaders;

    private boolean isModified = true;

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public boolean isModified() {
        return isModified;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}

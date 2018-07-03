package com.sc.github.view;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author shamschu
 * @Date 2018/3/1 下午5:40
 */
public class RemoteResource {

    // 资源输入流
    private InputStream inputStream;

    // 服务端相应头部信息
    private Map<String, List<String>> responseHeaders;

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

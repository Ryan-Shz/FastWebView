package com.ryan.github.view;

import java.util.Map;

/**
 * 远程请求配置职责
 *
 * @author Ryan
 * create by 2018/2/11 上午10:50
 */
public interface IRemoteRequestConfig {

    /**
     * 添加指定url的请求头
     *
     * @param url    指定url
     * @param header 请求头信息
     */
    void addHeader(String url, Map<String, String> header);

    /**
     * 设置初始请求地址
     *
     * @param url 初始请求地址
     */
    void setOriginalUrl(String url);

}

package com.ryan.github.view;

/**
 * 静态资源过滤器
 *
 * Created by Ryan
 * 2018/2/11 下午2:56
 */
public interface ExtensionFilter {

    /**
     * 是否需要过滤该类型的静态资源
     *
     * @param extension 静态资源类型, png、css、js等等
     * @return true过滤/false不过滤 若过滤，则不在使用本地缓存
     */
    boolean isFilter(String extension);

    /**
     * 添加要过滤的静态资源类型
     *
     * @param extension 静态资源类型
     */
    void addExtension(String extension);

    /**
     * 删除被过滤的静态资源类型
     *
     * @param extension 静态资源类型
     */
    void removeExtension(String extension);

    /**
     * 清除所有被过滤的静态资源类型
     */
    void clearExtension();

}

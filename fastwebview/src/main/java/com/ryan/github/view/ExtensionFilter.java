package com.ryan.github.view;

/**
 * filter some mime type resources without caching.
 * <p>
 * Created by Ryan
 * 2018/2/11 下午2:56
 */
public interface ExtensionFilter {

    boolean isFilter(String extension);

    void addExtension(String extension);

    void removeExtension(String extension);

    void clearExtension();

}

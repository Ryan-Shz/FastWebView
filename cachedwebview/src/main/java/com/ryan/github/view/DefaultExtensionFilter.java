package com.ryan.github.view;

import java.util.HashSet;
import java.util.Set;

/**
 * 磁盘存储过滤器
 * <p>
 * Created by Ryan
 * 2018/2/11 下午3:16
 */
public class DefaultExtensionFilter implements ExtensionFilter {

    private Set<String> mFilterExtensions;

    public DefaultExtensionFilter() {
        mFilterExtensions = new HashSet<>();
        addExtension("");
        addExtension("html");
        addExtension("mp4");
        addExtension("mp3");
        addExtension("ogg");
        addExtension("avi");
        addExtension("wmv");
        addExtension("flv");
        addExtension("rmvb");
        addExtension("3gp");
    }

    @Override
    public boolean isFilter(String extension) {
        return mFilterExtensions.contains(extension);
    }

    @Override
    public void addExtension(String extension) {
        mFilterExtensions.add(extension);
    }

    @Override
    public void removeExtension(String extension) {
        mFilterExtensions.remove(extension);
    }

    @Override
    public void clearExtension() {
        mFilterExtensions.clear();
    }

}

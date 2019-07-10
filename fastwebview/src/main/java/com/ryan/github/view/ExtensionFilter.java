package com.ryan.github.view;

import java.util.HashSet;
import java.util.Set;

/**
 * 磁盘存储过滤器
 *
 * @author Ryan
 * create by 2018/2/11 下午3:16
 */
public class ExtensionFilter implements IExtensionFilter {

    private Set<String> mFilterExtensions;

    public ExtensionFilter() {
        mFilterExtensions = new HashSet<>();
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

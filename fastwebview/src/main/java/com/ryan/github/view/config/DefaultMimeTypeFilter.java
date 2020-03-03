package com.ryan.github.view.config;

import java.util.HashSet;
import java.util.Set;

/**
 * default filter.
 * 可缓存资源白名单
 * <p>
 * Created by Ryan
 * 2018/2/11 下午3:16
 */
public class DefaultMimeTypeFilter implements MimeTypeFilter {

    private Set<String> mFilterMimeTypes;

    public DefaultMimeTypeFilter() {
        mFilterMimeTypes = new HashSet<>();
        // JavaScript
        addMimeType("application/javascript");
        addMimeType("application/ecmascript");
        addMimeType("application/x-ecmascript");
        addMimeType("application/x-javascript");
        addMimeType("text/ecmascript");
        addMimeType("text/javascript");
        addMimeType("text/javascript1.0");
        addMimeType("text/javascript1.1");
        addMimeType("text/javascript1.2");
        addMimeType("text/javascript1.3");
        addMimeType("text/javascript1.4");
        addMimeType("text/javascript1.5");
        addMimeType("text/jscript");
        addMimeType("text/livescript");
        addMimeType("text/x-ecmascript");
        addMimeType("text/x-javascript");
        // image
        addMimeType("image/gif");
        addMimeType("image/jpeg");
        addMimeType("image/png");
        addMimeType("image/svg+xml");
        addMimeType("image/bmp");
        addMimeType("image/webp");
        addMimeType("image/tiff");
        addMimeType("image/vnd.microsoft.icon");
        addMimeType("image/x-icon");
        // css
        addMimeType("text/css");
        // stream
        addMimeType("application/octet-stream");
    }

    @Override
    public boolean isFilter(String extension) {
        return !mFilterMimeTypes.contains(extension);
    }

    @Override
    public void addMimeType(String extension) {
        mFilterMimeTypes.add(extension);
    }

    @Override
    public void removeMimeType(String extension) {
        mFilterMimeTypes.remove(extension);
    }

    @Override
    public void clear() {
        mFilterMimeTypes.clear();
    }

}

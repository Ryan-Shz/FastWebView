package com.ryan.github.view.config;

import java.util.HashSet;
import java.util.Set;

/**
 * default filter.
 * <p>
 * Created by Ryan
 * 2018/2/11 下午3:16
 */
public class DefaultMimeTypeFilter implements MimeTypeFilter {

    private Set<String> mFilterMimeTypes;

    public DefaultMimeTypeFilter() {
        mFilterMimeTypes = new HashSet<>();
        addMimeType("text/html");
        // audio
        addMimeType("audio/mpeg");
        addMimeType("audio/midi");
        addMimeType("audio/webm");
        addMimeType("audio/ogg");
        addMimeType("audio/wave");
        addMimeType("audio/wav");
        addMimeType("audio/x-wav");
        addMimeType("audio/mp4");
        addMimeType("audio/x-pn-wav");
        addMimeType("audio/x-ms-wma");
        // video
        addMimeType("video/webm");
        addMimeType("video/ogg");
        addMimeType("video/x-msvideo");
        addMimeType("video/mp4");
        addMimeType("video/mpeg");
        addMimeType("video/quicktime");
        addMimeType("video/x-ms-wmv");
        addMimeType("video/x-flv");
        addMimeType("video/x-matroska");
    }

    @Override
    public boolean isFilter(String extension) {
        return mFilterMimeTypes.contains(extension);
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

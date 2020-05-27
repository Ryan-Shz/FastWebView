package com.ryan.github.view.config;

import android.content.Context;

import com.ryan.github.view.utils.AppVersionUtil;
import com.ryan.github.view.utils.MemorySizeCalculator;

import java.io.File;

/**
 * Created by Ryan
 * 2018/2/7 下午5:41
 */
public class CacheConfig {

    private String mCacheDir;
    private int mVersion;
    private long mDiskCacheSize;
    private int mMemCacheSize;
    private MimeTypeFilter mFilter;

    private CacheConfig() {

    }

    public String getCacheDir() {
        return mCacheDir;
    }

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public MimeTypeFilter getFilter() {
        return mFilter;
    }

    public long getDiskCacheSize() {
        return mDiskCacheSize;
    }

    public int getMemCacheSize() {
        return mMemCacheSize;
    }

    public static class Builder {

        private static final String CACHE_DIR_NAME = "cached_webview_force";
        private static final int DEFAULT_DISK_CACHE_SIZE = 100 * 1024 * 1024;
        private String cacheDir;
        private int version;
        private long diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        private int memoryCacheSize = MemorySizeCalculator.getSize();
        private MimeTypeFilter filter = new DefaultMimeTypeFilter();

        public Builder(Context context) {
            cacheDir = context.getCacheDir() + File.separator + CACHE_DIR_NAME;
            version = AppVersionUtil.getVersionCode(context);
        }

        public Builder setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setDiskCacheSize(long diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder setExtensionFilter(MimeTypeFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder setMemoryCacheSize(int memoryCacheSize) {
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public CacheConfig build() {
            CacheConfig config = new CacheConfig();
            config.mCacheDir = cacheDir;
            config.mVersion = version;
            config.mDiskCacheSize = diskCacheSize;
            config.mFilter = filter;
            config.mMemCacheSize = memoryCacheSize;
            return config;
        }
    }
}
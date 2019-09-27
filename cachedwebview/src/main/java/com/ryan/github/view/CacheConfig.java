package com.ryan.github.view;

/**
 * Created by Ryan
 * 2018/2/7 下午5:41
 */
public class CacheConfig {

    private String mCacheDir;
    private int mVersion;
    private long mDiskCacheSize;
    private ExtensionFilter mFilter;

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

    public ExtensionFilter getFilter() {
        return mFilter;
    }

    public long getDiskCacheSize() {
        return mDiskCacheSize;
    }

    public static class Builder {

        private String cacheDir;
        private int version;
        private long diskCacheSize;
        private ExtensionFilter filter;

        public Builder setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public void setDiskCacheSize(long diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
        }

        public Builder setExtensionFilter(ExtensionFilter filter) {
            this.filter = filter;
            return this;
        }

        public CacheConfig build() {
            CacheConfig config = new CacheConfig();
            config.mCacheDir = cacheDir;
            config.mVersion = version;
            config.mDiskCacheSize = diskCacheSize;
            config.mFilter = filter;
            return config;
        }
    }
}

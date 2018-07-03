package com.sc.github.view;

/**
 * @author shamschu
 * @Date 2018/2/7 下午5:41
 */
public class DiskCacheConfig {

    private String mCacheDir;
    private int mVersion;
    private long mCacheSize;
    private IExtensionFilter mFilter;

    private DiskCacheConfig() {

    }

    public String getCacheDir() {
        return mCacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.mCacheDir = cacheDir;
    }

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.mCacheSize = cacheSize;
    }

    public IExtensionFilter getFilter() {
        return mFilter;
    }

    public void setFilter(IExtensionFilter filter) {
        mFilter = filter;
    }

    public static class Builder {

        private String cacheDir;
        private int version;
        private long cacheSize;
        private IExtensionFilter filter;

        public Builder setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setCacheSize(long cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder setExtensionFilter(IExtensionFilter filter) {
            this.filter = filter;
            return this;
        }

        public DiskCacheConfig build() {
            DiskCacheConfig config = new DiskCacheConfig();
            config.mCacheDir = cacheDir;
            config.mVersion = version;
            config.mCacheSize = cacheSize;
            config.mFilter = filter;
            return config;
        }
    }
}

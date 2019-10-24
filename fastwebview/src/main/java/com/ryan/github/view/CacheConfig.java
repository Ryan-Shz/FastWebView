package com.ryan.github.view;

/**
 * Created by Ryan
 * 2018/2/7 下午5:41
 */
public class CacheConfig {

    private String mCacheDir;
    private int mVersion;
    private long mDiskCacheSize;
    private MimeTypeFilter mFilter;
    private boolean memoryCacheEnable = true;
    private int maxMemoryCacheSize;

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

    public int getMaxMemoryCacheSize() {
        return maxMemoryCacheSize;
    }

    public boolean isMemoryCacheEnable() {
        return memoryCacheEnable;
    }

    public static class Builder {

        private String cacheDir;
        private int version;
        private long diskCacheSize;
        private MimeTypeFilter filter;
        private boolean memoryCacheEnable = true;
        private int maxMemoryCacheSize;

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

        public Builder setMemoryCacheEnable(boolean enable){
            this.memoryCacheEnable = enable;
            return this;
        }

        public Builder setMaxMemoryCacheSize(int memoryCacheSize){
            this.maxMemoryCacheSize = memoryCacheSize;
            return this;
        }

        public CacheConfig build() {
            CacheConfig config = new CacheConfig();
            config.mCacheDir = cacheDir;
            config.mVersion = version;
            config.mDiskCacheSize = diskCacheSize;
            config.mFilter = filter;
            config.memoryCacheEnable = memoryCacheEnable;
            config.maxMemoryCacheSize = maxMemoryCacheSize;
            return config;
        }
    }
}
package com.ryan.github.view.utils;

/**
 * the default memory cache size calculator.
 * <p>
 * Created by Ryan
 * on 2020/4/14
 */
public class MemorySizeCalculator {

    private static final int MB = 1024 * 1024;
    private static final int MB_15 = 15 * MB;
    private static final int MB_10 = 10 * MB;
    private static final int MB_5 = 5 * MB;

    public static int getSize() {
        long maxMemorySize = Runtime.getRuntime().maxMemory();
        int maxSizeByMB = (int) (maxMemorySize / MB);
        if (maxSizeByMB >= 512) {
            return MB_15;
        }
        if (maxSizeByMB >= 256) {
            return MB_10;
        }
        if (maxSizeByMB > 128) {
            return MB_5;
        }
        return 0;
    }
}

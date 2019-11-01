package com.ryan.github.view.utils;

import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Reusable input stream
 * <p>
 * Created by Ryan
 * at 2019/10/8
 */
public class ReusableInputStream extends BufferedInputStream {

    public ReusableInputStream(@NonNull InputStream in) {
        super(in);
        mark(Integer.MAX_VALUE);
    }
}

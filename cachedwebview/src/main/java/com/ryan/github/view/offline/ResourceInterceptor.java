package com.ryan.github.view.offline;

import android.webkit.WebResourceResponse;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public interface ResourceInterceptor {

    WebResourceResponse load(Chain chain);

}

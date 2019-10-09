package com.ryan.github.view.offline;

import com.ryan.github.view.WebResource;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public interface ResourceInterceptor {

    WebResource load(Chain chain);

}

package com.ryan.github.view.loader;

import com.ryan.github.view.WebResource;

/**
 * Created by Ryan
 * 2018/2/7 下午7:53
 */
public interface ResourceLoader {

    WebResource getResource(SourceRequest request);

}




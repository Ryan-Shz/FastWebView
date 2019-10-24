package com.ryan.github.view.offline;

import android.webkit.WebResourceResponse;

import com.ryan.github.view.WebResource;

/**
 * Created by Ryan
 * at 2019/10/8
 */
public interface WebResourceResponseGenerator {

    WebResourceResponse generate(WebResource resource, String urlMime);

}

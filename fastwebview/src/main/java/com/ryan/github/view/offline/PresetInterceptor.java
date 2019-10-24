package com.ryan.github.view.offline;

import com.ryan.github.view.WebResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Ryan
 * on 2019/10/24
 */
public class PresetInterceptor implements ResourceInterceptor {

    @Override
    public WebResource load(Chain chain) {
        // read origin bytes
        WebResource resource = chain.process(chain.getRequest());
        if (resource != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedSink sink = Okio.buffer(Okio.sink(bos));
            try {
                InputStream inputStream = resource.getInputStream();
                inputStream.reset();
                sink.writeAll(Okio.source(inputStream));
                sink.flush();
                resource.setOriginBytes(bos.toByteArray());
                inputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }
}

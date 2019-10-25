package com.ryan.github.view.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * Created by Ryan
 * on 2019/10/25
 */
public class HeaderUtils {

    public static Map<String, String> generateHeadersMap(Headers headers) {
        Map<String, String> headersMap = new HashMap<>();
        int index = 0;
        for (String key : headers.names()) {
            StringBuilder values = new StringBuilder();
            for (String value : headers.values(key)) {
                values.append(value);
                if (index++ > 0) {
                    values.append(",");
                }
            }
            index = 0;
            headersMap.put(key, values.toString());
        }
        return headersMap;
    }

    public static Map<String, String> generateHeadersMap(Map<String, List<String>> headers) {
        Map<String, String> headersMap = new HashMap<>();
        int index = 0;
        for (String key : headers.keySet()) {
            StringBuilder values = new StringBuilder();
            for (String value : headers.get(key)) {
                values.append(value);
                if (index++ > 0) {
                    values.append(",");
                }
            }
            index = 0;
            headersMap.put(key, values.toString());
        }
        return headersMap;
    }
}

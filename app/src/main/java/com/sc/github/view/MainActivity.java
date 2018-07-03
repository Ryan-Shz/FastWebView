package com.sc.github.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FastWebView fastWebView = findViewById(R.id.fast_web_view);
        fastWebView.setDiskCacheEnable(true);
        String url = "http://www.baidu.com/";
        fastWebView.loadUrl(url);
    }
}

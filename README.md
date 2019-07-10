# FastWebView
拦截静态资源加载，自定义本地缓存策略的WebView, 大幅提高WebView加载速度, 同时支持离线加载和预加载。

## 使用方法
替换原生的WebView, 并打开缓存开关。

### 打开缓存开关
```
FastWebView fastWebView = findViewById(R.id.fast_web_view);
fastWebView.setDiskCacheEnable(true);
```
若不设置，则默认开关为关，此WebView等同于普通WebView。

### 执行JS脚本
```
fastWebView.runJs(String function, Object... args);
```

### 首次加载
首次加载需要完整加载整个H5页面，所以加载速度跟普通webview一样，如果想提高首次加载速度，可以使用preload来预加载页面。
```
FastWebView.preload(Context context, String url)
```

### 如何更新静态资源
由于FastWebView是强制缓存所有静态资源到本地并优先加载本地静态资源，所以如果需要更新静态资源，可以和你的前端约定好，
当静态资源更新时，直接修改静态资源地址，一般为小版本升级，如http://xxxxxx//xx_v1.0.png --> http://xxxxxx//xx_v1.1.png

# 适用场景
1. 静态资源更新频率低
2. 需要支持离线加载
3. 对加载速度要求高

源码很清楚，基于源码可自行扩展

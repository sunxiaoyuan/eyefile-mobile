package com.simon.margaret.delegates.web.chromeclient;


import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.simon.margaret.ui.loader.MargaretLoader;


/**
 * Created by apple on 2019/8/22.
 */

public class WebChromeClientImpl extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        if (i == 100) {
            // 展示顶层webview
            float alpha = webView.getAlpha();
            if (alpha != 1) {
                webView.setAlpha(1);
            }
            MargaretLoader.stopLoading();
        }
    }


}


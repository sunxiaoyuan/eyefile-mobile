package com.simon.margaret.delegates.web.client;

import android.graphics.Bitmap;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.StringUtils;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.web.IPageLoadListener;
import com.simon.margaret.delegates.web.WebDelegate;
import com.simon.margaret.delegates.web.route.Router;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.simon.margaret.util.log.MargaretLogger;
import com.simon.margaret.util.storage.MargaretPreference;

/**
 * Created by apple on 2019/8/22.
 */

public class WebViewClientImpl extends WebViewClient {
	private final WebDelegate DELEGATE;
	private IPageLoadListener mIPageLoadListener = null;
	private static final Handler HANDLER = Margaret.getHandler();

	public void setPageLoadListener(IPageLoadListener listener) {
		this.mIPageLoadListener = listener;
	}

	public WebViewClientImpl(WebDelegate delegate) {
		this.DELEGATE = delegate;
	}

	// 如果在页面中出现<a>标签或者调用href.loacation跳转页面
	// 一律交由原生组件处理页面跳转
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		MargaretLogger.d("shouldOverrideUrlLoading", url);
		return Router.getInstance().handleWebUrl(DELEGATE, url);
	}


	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (mIPageLoadListener != null) {
			mIPageLoadListener.onLoadStart();
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		syncCookie();
		if (mIPageLoadListener != null) {
			mIPageLoadListener.onLoadEnd();
		}

		if (!StringUtils.isEmpty(url)){
			if (url.endsWith("clinic")){
				@SuppressWarnings("unchecked")
				final IGlobalCallback<WebView> callback = CallbackManager
						.getInstance()
						.getCallback(CallbackType.ON_WEBVIEW_READY_CLINIC);
				if (callback != null) {
					callback.executeCallback(view);
				}
			} else if (url.endsWith("school")){
				@SuppressWarnings("unchecked")
				final IGlobalCallback<WebView> callback = CallbackManager
						.getInstance()
						.getCallback(CallbackType.ON_WEBVIEW_READY_SCHOOL);
				if (callback != null) {
					callback.executeCallback(view);
				}
			}
		}



	}

	// 获取浏览器cookie
	private void syncCookie() {
		final CookieManager manager = CookieManager.getInstance();
		/*
		  注意，这里的Cookie和API请求的Cookie是不一样的，这个在网页不可见
         */
		final String webHost = Margaret.getConfiguration(ConfigKeys.WEB_HOST);
		if (webHost != null) {
			if (manager.hasCookies()) {
				final String cookieStr = manager.getCookie(webHost);
				if (cookieStr != null && !cookieStr.equals("")) {
					MargaretPreference.addCustomAppProfile("cookie", cookieStr);
				}
			}
		}
	}


}


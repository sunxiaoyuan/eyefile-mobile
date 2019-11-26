package com.sgeye.exam.android.modules.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.modules.bottom.BottomDelegate;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.app.UserInfo;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.delegates.web.WebDelegateImpl;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2019/9/4.
 */

public class SignInHyberDelegate extends MargaretDelegate {


	@Override
	public Object setLayout() {
		return R.layout.delegate_hyber_signin;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

		// js登录成功之后的回调
		CallbackManager.getInstance().addCallback(CallbackType.ON_JS_CALL_NATIVE_LOGIN, args -> {
			// 在这里取出用户登录信息，保存到本地
			AccountManager.setSignState(true);
			final CookieManager manager = CookieManager.getInstance();
			final String webHost = Margaret.getConfiguration(ConfigKeys.WEB_HOST);
			if (manager.hasCookies()) {
				Map<String, Object> userInfoMap = new HashMap<>();
				final String cookieStr = manager.getCookie(webHost);
				if (cookieStr != null && !cookieStr.equals("")) {
					String[] split = cookieStr.split(";");
					for (String str : split) {
						String[] kv = str.split("=");
						userInfoMap.put(kv[0], kv[1]);
					}
					JSONObject json = new JSONObject(userInfoMap);
					UserInfo userInfo = JSON.toJavaObject(json, UserInfo.class);
					Margaret.saveCurrentUser(userInfo);
				}
			}

			getSupportDelegate().startWithPop(new BottomDelegate());

//			// 通知主线程跳转页面
//			Handler mainHandler = new Handler(Looper.getMainLooper());
//			mainHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					// 跳转到内部
//					Margaret.getTopFragment().startWithPop();
//				}
//			});
		});
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		final WebDelegateImpl delegate = WebDelegateImpl.create("http://test.eyefile.cn/phone/#/login");
//		final WebDelegateImpl delegate = WebDelegateImpl.create("http://192.168.31.161:7783/phone/#/login");
		delegate.setTopDelegate(SignInHyberDelegate.this.getParentDelegate());
		getSupportDelegate().loadRootFragment(R.id.web_signin_container, delegate);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}

package com.sgeye.exam.android.modules.my;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.modules.bottom.BottomDelegate;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.sgeye.exam.android.modules.signin.SignInHyberDelegate;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.app.UserInfo;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.observer.ObserverListener;
import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.WebSocketHandler;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by apple on 2019/9/4.
 */

public class MyDelegate extends BottomItemDelegate implements ObserverListener {


	@BindView(R2.id.tv_my_method)
	TextView methodTV;
	@BindView(R2.id.tv_my_name)
	TextView nameTV;
	@BindView(R2.id.tv_my_account)
	TextView accountTV;
	@BindView(R2.id.tv_my_phone)
	TextView phoneTV;

	@BindView(R2.id.ll_list)
	LinearLayout connDeviceLL;
	@BindView(R2.id.ll_list2)
	LinearLayout countMethodLL;
	@BindView(R2.id.tv_my_conStat)
	TextView myConStatTV;

	@Override
	public Object setLayout() {
		return R.layout.delegate_my;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

		ObserverManager.getInstance().add(this);

		if (WebSocketHandler.getDefault().isConnect()) {
			myConStatTV.setText("已连接");
		} else {
			myConStatTV.setText("未连接");
		}
	}

	@Override
	public void onDestroy() {
		ObserverManager.getInstance().remove(this);
		super.onDestroy();
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);

		UserInfo currentUser = Margaret.getCurrentUser();
		if (currentUser != null) {
			String name = currentUser.getName();
			String phone = currentUser.getMobile();
			String account = currentUser.getAccount();
			String method = currentUser.getSightStandard();
			if (!StringUtils.isEmpty(name)) {
				nameTV.setText(name);
			}
			if (!StringUtils.isEmpty(phone)) {
				phoneTV.setText(name);
			}
			if (!StringUtils.isEmpty(account)) {
				accountTV.setText(name);
			}
			if ("0".equals(method)) {
				methodTV.setText("对数记数法");
			} else {
				methodTV.setText("小数记数法");
			}
		}
	}

	@Override
	public void observerUpData(Object msg) {
		String note = (String) msg;
		String[] split = note.split(":");
		if (split[0].equals("onConnected")) {
			if ("true".equals(split[1])) {
				// on
				myConStatTV.setText("已连接");
			} else {
				// off
				myConStatTV.setText("未连接");
			}
		}
	}

	// 点击设备连接，页面跳转
	@OnClick(R2.id.ll_list)
	public void jump2ConnDevicePage() {
		final BottomDelegate bottomDelegate = getParentDelegate();
		MargaretDelegate connDevicePage = new ConDeviceDelegate();
		bottomDelegate.start(connDevicePage);
	}

	// 点击视力计数法，页面跳转
	@OnClick(R2.id.ll_list2)
	public void jump2CountMethodPage() {
		ToastUtils.showLong("视力计数法不允许修改");
	}

	// 退出登录
	@OnClick(R2.id.btn_my_logout)
	public void logout() {
		// 清除本地信息
		AccountManager.setSignState(false);

		Margaret.eraseLocalUserInfo();
		// 清除cookie
		MargaretPreference.addCustomAppProfile("cookie", null);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
				@Override
				public void onReceiveValue(Boolean value) {

				}
			});
		}
		// 跳转到登录页
		getParentDelegate().getSupportDelegate().startWithPop(new SignInHyberDelegate());
	}

}

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
import com.sgeye.exam.android.modules.check.ResultDialog;
import com.sgeye.exam.android.modules.signin.SignInHyberDelegate;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.app.UserInfo;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.observer.ObserverListener;
import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.WebSocketHandler;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by apple on 2019/9/4.
 */

public class MyDelegate extends BottomItemDelegate
		implements ObserverListener, OnChangeRefrection, OnChangePrintSettings, OnChangeOptometry {

	private int mRefrectionIndex = 0;
	private int mPrintSettingsIndex = 0;
	private int mOptometryIndex = 0;
	private List<String> mRefractionList = Arrays.asList("伟伦", "索维", "莫廷", "其他");
	private List<String> mPrintSettingList = Arrays.asList("不打印", "打印带回执", "打印不带回执");
	private List<String> mOptometryList = Arrays.asList("拓普康", "索维", "尼德克",
			"天乐", "新缘", "法里奥", "其他");

	@BindView(R2.id.tv_my_method)
	TextView methodTV;
	@BindView(R2.id.tv_my_name)
	TextView nameTV;
	@BindView(R2.id.tv_my_account)
	TextView accountTV;
	@BindView(R2.id.tv_my_phone)
	TextView phoneTV;

	@BindView(R2.id.ll_list_device)
	LinearLayout connDeviceLL;
	@BindView(R2.id.ll_list2)
	LinearLayout countMethodLL;
	@BindView(R2.id.tv_my_conStat)
	TextView myConStatTV;

	@BindView(R2.id.tv_refraction_check)
	TextView tv_refraction_check;

	@BindView(R2.id.tv_print_settings)
	TextView tv_print_settings;

	@BindView(R2.id.tv_optometry)
	TextView tv_optometry;


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
				phoneTV.setText(phone);
			}
			if (!StringUtils.isEmpty(account)) {
				accountTV.setText(account);
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
	@OnClick(R2.id.ll_list_device)
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

	@OnClick(R2.id.ll_list_quguang)
	public void changeRefractionCheck() {
		RefractionCheckDialog.Builder builder = new RefractionCheckDialog.Builder(getContext());
		builder.onChangeRefrection(this).distance(mRefrectionIndex).create().show();
	}

	@Override
	public void onChangeRefrection(int index) {
		mRefrectionIndex = index;
		tv_refraction_check.setText(mRefractionList.get(index));
	}

	@OnClick(R2.id.ll_list_print_settings)
	public void changePrintSettings() {
		PrintSettingsDialog.Builder builder = new PrintSettingsDialog.Builder(getContext());
		builder.onChangePrintSettings(this).distance(mPrintSettingsIndex).create().show();
	}

	@Override
	public void onChangePrintSettings(int index) {
		mPrintSettingsIndex = index;
		tv_print_settings.setText(mPrintSettingList.get(index));
	}

	@OnClick(R2.id.ll_list_pc_yanguang)
	public void changeOptometry() {
		OptometryDialog.Builder builder = new OptometryDialog.Builder(getContext());
		builder.onChangeOptometry(this).distance(mOptometryIndex).create().show();
	}

	@Override
	public void onChangeOptometry(int index) {
		mOptometryIndex = index;
		tv_optometry.setText(mOptometryList.get(index));
	}
}

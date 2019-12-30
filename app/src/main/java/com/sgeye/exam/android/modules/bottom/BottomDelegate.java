package com.sgeye.exam.android.modules.bottom;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.sgeye.exam.android.R;
import com.sgeye.exam.android.modules.clinic.ClinicHyberDelegate;
import com.sgeye.exam.android.modules.my.MyDelegate;
import com.sgeye.exam.android.modules.school.SchoolHyberDelegate;
import com.sgeye.exam.android.modules.signin.SignInHyberDelegate;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.ui.camera.MargaretCamera;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.storage.MargaretPreference;

import java.util.LinkedHashMap;

/**
 * Created by sunzhongyuan on 2018/10/13.
 * 底部栏delegate
 */

public class BottomDelegate extends BaseBottomDelegate {

	// 设置item
	@Override
	public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
		final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
		items.put(new BottomTabBean(R.mipmap.item00, R.mipmap.hitem00, "门诊筛查"), new ClinicHyberDelegate());
		items.put(new BottomTabBean(R.mipmap.item01, R.mipmap.hitem01, "学校筛查"), new SchoolHyberDelegate());
		items.put(new BottomTabBean(R.mipmap.item02, R.mipmap.hitem02, "我的"), new MyDelegate());
		return builder.addItems(items).build();
	}

	@Override
	public int setIndexDelegate() {
		return 0;
	}

	@Override
	public int setClickedColor() {
		return Color.parseColor("#00A97E");
	}

	@Override
	public int setUnClickedColor() {
		return Color.parseColor("#CDCFE1");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CallbackManager.getInstance().addCallback(CallbackType.ON_BACK_SIGN_OUT,
				args -> {
					// 清除本地信息
					AccountManager.setSignState(false);

					Margaret.eraseLocalUserInfo();
					// 清除cookie
					MargaretPreference.addCustomAppProfile("cookie", null);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						CookieManager.getInstance().removeAllCookies(value -> {

						});
					}
					getSupportDelegate().startWithPop(new SignInHyberDelegate());
				});
	}
}

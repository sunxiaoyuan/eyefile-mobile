package com.sgeye.exam.android.modules.check;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.sgeye.exam.android.R;
import com.sgeye.exam.android.event.bean.CheckPadEventBean;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.web.WebDelegateImpl;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by apple on 2019/11/27.
 */

public class EyeSightCheckManualDelegate extends BottomItemDelegate {

	private WebView currentWebView = null;
	private CheckPadEventBean bean = null;

	public void setBean(CheckPadEventBean bean) {
		this.bean = bean;
	}

	@Override
	public Object setLayout() {
		return R.layout.delegate_hyber_signin;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

		final WebDelegateImpl delegate =
				WebDelegateImpl.create(Margaret.getConfiguration(ConfigKeys.WEB_HOST) + "/phone/#/sight");
		delegate.setTopDelegate(EyeSightCheckManualDelegate.this.getParentDelegate());
		getSupportDelegate().loadRootFragment(R.id.web_signin_container, delegate);

		// 处理页面加载完成回调
		CallbackManager.getInstance()
				.addCallback(CallbackType.ON_WEBVIEW_READY_SIGHT, webView -> currentWebView = (WebView) webView);

		// 处理返回上一页回调
		CallbackManager.getInstance()
				.addCallback(CallbackType.ON_BACK_SIGHT_CHECK, args -> getSupportDelegate().pop());
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		// 给网页传递studentId、etaskId
		// 调用js方法，并且传参给页面
		if (currentWebView != null) {
			String params = bean.getStudentId() + "," + bean.getEtaskId();
			currentWebView.post(() -> currentWebView.loadUrl("javascript:onSightCheckManual('" + params + "')"));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return new DefaultHorizontalAnimator();
	}
}

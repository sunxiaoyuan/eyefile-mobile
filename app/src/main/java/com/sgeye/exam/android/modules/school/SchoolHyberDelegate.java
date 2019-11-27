package com.sgeye.exam.android.modules.school;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.camera.bean.CheckPadEventBean;
import com.sgeye.exam.android.constants.AppConstants;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.sgeye.exam.android.modules.check.EyeSightCheckDelegate;
import com.sgeye.exam.android.modules.scanner.ScannerDelegate;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.web.WebDelegateImpl;
import com.simon.margaret.net.RestClient;
import com.simon.margaret.ui.camera.MargaretCamera;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.log.MargaretLogger;
import com.zhangke.websocket.WebSocketHandler;

/**
 * Created by apple on 2019/9/4.
 */

public class SchoolHyberDelegate extends BottomItemDelegate {

	private WebView currentWebView = null;

	@Override
	public Object setLayout() {
		return R.layout.delegate_hyber_signin;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

		final WebDelegateImpl delegate =
				WebDelegateImpl.create(Margaret.getConfiguration(ConfigKeys.WEB_HOST) + "/phone/#/school");
		delegate.setTopDelegate(SchoolHyberDelegate.this.getParentDelegate());
		getSupportDelegate().loadRootFragment(R.id.web_signin_container, delegate);

		// 处理页面加载完成回调
		CallbackManager.getInstance().
				addCallback(CallbackType.ON_WEBVIEW_READY_SCHOOL, webView -> currentWebView = (WebView) webView);

		handleQrCodeScan();

		handleTakePhoto();

		handleEysSightCheck();

	}

	private void handleEysSightCheck() {
		CallbackManager.getInstance().addCallback(CallbackType.ON_JS_CALL_NATIVE_SIGHT_CHECK_SCHOOL, args -> {

			CheckPadEventBean bean = (CheckPadEventBean) args;
			// on - 进入视力检查页面
			EyeSightCheckDelegate delegate = new EyeSightCheckDelegate();
			delegate.setBean(bean);
			getParentDelegate().getSupportDelegate().start(delegate);
			// 通知pad页面跳转
			WebSocketHandler.getDefault().send(AppConstants.SOCKET_SEND_CHANGE_PAGE);
		});
	}

	private void handleTakePhoto() {

		// 调起相机拍照
		CallbackManager.getInstance().addCallback(CallbackType.ON_JS_CALL_NATIVE_TAKE_PHOTO_SCHOOL, args -> {
			MargaretCamera.start(this);
		});

		// 处理相机拍照回调
		CallbackManager.getInstance()
				.addCallback(CallbackType.ON_CROP, args -> {
					// 获取本地图片地址
					Uri picPath = (Uri) args;
					// 上传图片
					RestClient.builder()
							.url(Margaret.getConfiguration(ConfigKeys.API_HOST) + "/efile/image")
							.loader(_mActivity)
							.file(picPath.getPath())
							.success(response -> {
								JSONObject parseObject = JSON.parseObject(response);
								int code = (int) parseObject.get("code");
								if (code == 0) {
									// 上传成功
									String picUrl = (String) parseObject.get("data");
									// 调用js方法，并且传参给页面
									if (currentWebView != null) {
										currentWebView.post(() -> currentWebView.loadUrl("javascript:onUploadPicSuccess('" + picUrl + "')"));
									}
								} else {
									// 上传失败
									ToastUtils.showShort("上传图片失败" + parseObject.get("message"));
								}
							})
							.error((code, msg) -> {
								ToastUtils.showShort("上传图片失败\n" + msg);
							})
							.build()
							.upload();
				});
	}


	private void handleQrCodeScan() {
		// 调起二维码页面
		CallbackManager.getInstance().addCallback(CallbackType.ON_JS_CALL_NATIVE_QR_SCHOOL, args -> {
			ScannerDelegate scannerDelegate = new ScannerDelegate();
			scannerDelegate.setCallbackType(CallbackType.ON_SCHOOL_SCAN);
			getParentDelegate().getSupportDelegate().start(scannerDelegate);
		});

		// 处理二维码扫描回调
		CallbackManager.getInstance().addCallback(CallbackType.ON_SCHOOL_SCAN, args -> {
			final String ret = (String) args;
			if (!"".equals(args)) {
				// 调用js方法，并且传参
				if (currentWebView != null) {
					currentWebView.post(() -> currentWebView.loadUrl("javascript:onScanSuccess('" + ret + "')"));
				}
			}
		});
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);

	}

}

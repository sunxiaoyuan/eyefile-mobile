package com.sgeye.exam.android.tasks;

import com.sgeye.exam.android.camera.CallNativeBackEvent;
import com.sgeye.exam.android.camera.CallNativeCameraEvent;
import com.sgeye.exam.android.camera.CallNativeCheckPadEvent;
import com.sgeye.exam.android.camera.CallNativeSightCheckEvent;
import com.sgeye.exam.android.camera.CallNativeUploadPicEvent;
import com.sgeye.exam.android.modules.signin.SignInSuccessEvent;
import com.sgeye.exam.android.toast.CallNativeToastEvent;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.net.interceptors.AddCookieInterceptor;
import com.simon.margaret.net.interceptors.BaseInterceptor;
import com.simon.margaret.util.launchstarter.task.MainTask;

/**
 * Created by apple on 2019/11/19.
 */

public class InitAppConfigTask extends MainTask {

	@Override
	public void run() {

		Margaret.init(mContext)
				.withApiHost("https://api.okjing.net")
				.withWebHost("https://eyefile.cn")
//				.withApiHost("http://api2.okjing.net")
//				.withWebHost("http://test.eyefile.cn")
 				.withWebEvent("callNativeCamera", new CallNativeCameraEvent()) // 调起二维码
				.withWebEvent("callNativeUploadPic", new CallNativeUploadPicEvent()) // 上传图片
				.withWebEvent("signInSuccess", new SignInSuccessEvent()) // 登录成功
				.withWebEvent("callNativeToast", new CallNativeToastEvent()) // 调用原生toast
				.withWebEvent("callNativeCheckPad", new CallNativeCheckPadEvent()) // 检查pad、连接状态
				.withWebEvent("callNativeBack", new CallNativeBackEvent()) // 调用原生返回
				.withWebEvent("callNativeSightCheck", new CallNativeSightCheckEvent()) // 跳转自动检查页面
				.withJavascriptInterface("peanut")
				.withInterceptor(new AddCookieInterceptor())
				.withInterceptor(new BaseInterceptor())
				.configure();

	}


}

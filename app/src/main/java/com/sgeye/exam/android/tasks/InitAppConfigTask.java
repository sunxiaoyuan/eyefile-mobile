package com.sgeye.exam.android.tasks;

import com.sgeye.exam.android.camera.CallNativeCameraEvent;
import com.sgeye.exam.android.camera.CallNativeCheckPadEvent;
import com.sgeye.exam.android.camera.CallNativeUploadPicEvent;
import com.sgeye.exam.android.modules.signin.SignInSuccessEvent;
import com.sgeye.exam.android.toast.CallNativeToastEvent;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.net.interceptors.AddCookieInterceptor;
import com.simon.margaret.net.interceptors.BaseInterceptor;
import com.simon.margaret.util.launchstarter.task.MainTask;
import com.simon.margaret.util.launchstarter.task.Task;
import com.simon.margaret.util.storage.MargaretPreference;

/**
 * Created by apple on 2019/11/19.
 */

public class InitAppConfigTask extends MainTask {

	@Override
	public void run() {

		Margaret.init(mContext)
				.withApiHost("http://api2.okjing.net")
				.withImageUploadUrl("http://api2.okjing.net/efile/image")
				.withWebHost("http://test.eyefile.cn")
//				.withWebHost("http://192.168.31.161:7783")
				.withWebEvent("callNativeCamera", new CallNativeCameraEvent())
				.withWebEvent("callNativeUploadPic", new CallNativeUploadPicEvent())
				.withWebEvent("signInSuccess", new SignInSuccessEvent())
				.withWebEvent("callNativeToast", new CallNativeToastEvent())
				.withWebEvent("callNativeCheckPad", new CallNativeCheckPadEvent())
				.withJavascriptInterface("peanut")
				.withInterceptor(new AddCookieInterceptor())
				.withInterceptor(new BaseInterceptor())
				.configure();

	}


}

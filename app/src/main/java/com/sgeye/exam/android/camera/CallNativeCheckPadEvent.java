package com.sgeye.exam.android.camera;

import com.alibaba.fastjson.JSON;
import com.sgeye.exam.android.camera.bean.CheckPadEventBean;
import com.sgeye.exam.android.camera.bean.ScanEventBean;
import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeCheckPadEvent extends Event {

	@Override
	public String execute(String params) {

		CheckPadEventBean bean = JSON.parseObject(params, CheckPadEventBean.class);

		@SuppressWarnings("unchecked")		final IGlobalCallback<CheckPadEventBean> callback = CallbackManager
				.getInstance()
				.getCallback(CallbackType.ON_JS_CALL_NATIVE_CHECK_PAD);
		if (callback != null) {
			callback.executeCallback(bean);
		}

		return null;
	}

}

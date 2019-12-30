package com.sgeye.exam.android.event;

import com.alibaba.fastjson.JSON;
import com.sgeye.exam.android.event.bean.UploadImgEventBean;
import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeUploadPicEvent extends Event {

	@Override
	public String execute(String params) {

		CallbackType type = null;
		// json解析
		UploadImgEventBean bean = JSON.parseObject(params, UploadImgEventBean.class);
		if ("clinic".equals(bean.getFrom())) {
			type = CallbackType.ON_JS_CALL_NATIVE_TAKE_PHOTO_CLINIC;
		} else if ("school".equals(bean.getFrom())) {
			type = CallbackType.ON_JS_CALL_NATIVE_TAKE_PHOTO_SCHOOL;
		}

		if (type != null) {
			@SuppressWarnings("unchecked")		final IGlobalCallback<String> callback = CallbackManager
					.getInstance()
					.getCallback(type);
			if (callback != null) {
				callback.executeCallback(params);
			}
		}

		return null;
	}

}

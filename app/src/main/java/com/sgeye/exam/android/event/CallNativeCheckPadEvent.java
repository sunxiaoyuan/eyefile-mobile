package com.sgeye.exam.android.event;

import com.simon.margaret.delegates.web.event.Event;
import com.zhangke.websocket.WebSocketHandler;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeCheckPadEvent extends Event {

	@Override
	public String execute(String params) {

//		CheckPadEventBean bean = JSON.parseObject(params, CheckPadEventBean.class);
//
//		@SuppressWarnings("unchecked")		final IGlobalCallback<CheckPadEventBean> callback = CallbackManager
//				.getInstance()
//				.getCallback(CallbackType.ON_JS_CALL_NATIVE_CHECK_PAD);
//		if (callback != null) {
//			callback.executeCallback(bean);
//		}

		// 检查socket连接状态
		if (WebSocketHandler.getDefault().isConnect()) {
			return "true";
		} else {
			return "false";
		}
	}

}

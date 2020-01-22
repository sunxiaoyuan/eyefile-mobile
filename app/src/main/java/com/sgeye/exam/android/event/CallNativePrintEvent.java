package com.sgeye.exam.android.event;

import com.alibaba.fastjson.JSON;
import com.sgeye.exam.android.event.bean.CheckPadEventBean;
import com.sgeye.exam.android.event.bean.PrintEventBean;
import com.sgeye.exam.android.event.bean.ScanEventBean;
import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativePrintEvent extends Event {

	@Override
	public String execute(String params) {

		PrintEventBean bean = JSON.parseObject(params, PrintEventBean.class);
		CallbackType type = null;

		if ("clinic".equals(bean.getFrom())) {
			type = CallbackType.ON_JS_CALL_NATIVE_PRINT_CLINIC;
		} else if ("school".equals(bean.getFrom())) {
			type = CallbackType.ON_JS_CALL_NATIVE_PRINT_SCHOOL;
		}

		if (type != null) {
			@SuppressWarnings("unchecked")        final IGlobalCallback<PrintEventBean> callback = CallbackManager
					.getInstance()
					.getCallback(type);
			if (callback != null) {
				callback.executeCallback(bean);
			}
		}
		return null;
	}

}

package com.sgeye.exam.android.event;

import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeSignoutEvent extends Event {

	@Override
	public String execute(String params) {

		@SuppressWarnings("unchecked") final IGlobalCallback<String> callback = CallbackManager
				.getInstance()
				.getCallback(CallbackType.ON_BACK_SIGN_OUT);
		if (callback != null) {
			callback.executeCallback(params);
		}
		return null;
	}

}

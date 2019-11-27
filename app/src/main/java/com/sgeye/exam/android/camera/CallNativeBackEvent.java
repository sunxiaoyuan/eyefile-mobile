package com.sgeye.exam.android.camera;

import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeBackEvent extends Event {

	@Override
	public String execute(String params) {


		@SuppressWarnings("unchecked")        final IGlobalCallback<String> callback = CallbackManager
				.getInstance()
				.getCallback(CallbackType.ON_BACK_SIGHT_CHECK);
		if (callback != null) {
			callback.executeCallback(params);
		}


		return null;
	}

}

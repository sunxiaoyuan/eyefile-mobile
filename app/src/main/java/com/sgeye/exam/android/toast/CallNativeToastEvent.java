package com.sgeye.exam.android.toast;

import android.widget.Toast;

import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.web.event.Event;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativeToastEvent extends Event {

	@Override
	public String execute(String params) {
		Toast.makeText(Margaret.getApplicationContext(), params, Toast.LENGTH_SHORT).show();
		return null;
	}

}

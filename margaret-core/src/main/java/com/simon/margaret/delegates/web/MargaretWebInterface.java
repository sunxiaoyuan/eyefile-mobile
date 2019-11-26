package com.simon.margaret.delegates.web;


import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.simon.margaret.delegates.web.event.Event;
import com.simon.margaret.delegates.web.event.EventManager;
import com.simon.margaret.util.log.MargaretLogger;

/**
 * Created by apple on 2019/8/22.
 */

public class MargaretWebInterface {

	private final WebDelegate DELEGATE;

	private MargaretWebInterface(WebDelegate delegate) {
		this.DELEGATE = delegate;
	}

	static MargaretWebInterface create(WebDelegate delegate) {
		return new MargaretWebInterface(delegate);
	}

	@SuppressWarnings("unused")
	@JavascriptInterface
	public String event(String params) {
		final String action = JSON.parseObject(params).getString("action");
		final Event event = EventManager.getInstance().createEvent(action);
		MargaretLogger.d("WEB_EVENT", params);
		if (event != null) {
			event.setAction(action);
			event.setDelegate(DELEGATE);
			event.setContext(DELEGATE.getContext());
			event.setUrl(DELEGATE.getUrl());
			return event.execute(params);
		}
		return null;
	}

}

package com.sgeye.exam.android.event;

import com.alibaba.fastjson.JSON;
import com.sgeye.exam.android.event.bean.PrintEventBean;
import com.sgeye.exam.android.event.bean.ScanEventBean;
import com.simon.margaret.delegates.web.event.Event;

/**
 * Created by apple on 2019/11/19.
 */

public class CallNativePrintEvent extends Event {

	@Override
	public String execute(String params) {

		PrintEventBean bean = JSON.parseObject(params, PrintEventBean.class);

		return null;
	}

}

package com.sgeye.exam.android.event.bean;

/**
 * Created by apple on 2019/11/25.
 */

public class ScanEventBean {

	private String action;
	private String from;

	public ScanEventBean() {
	}

	public ScanEventBean(String action, String from) {
		this.action = action;
		this.from = from;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}

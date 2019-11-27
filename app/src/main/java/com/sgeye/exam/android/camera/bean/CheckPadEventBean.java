package com.sgeye.exam.android.camera.bean;

/**
 * Created by apple on 2019/11/25.
 */

public class CheckPadEventBean {

	private String action;
	private String studentId;
	private String etaskId;
	private String from;

	public CheckPadEventBean() {
	}

	public CheckPadEventBean(String action, String studentId, String etaskId, String from) {
		this.action = action;
		this.studentId = studentId;
		this.etaskId = etaskId;
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getEtaskId() {
		return etaskId;
	}

	public void setEtaskId(String etaskId) {
		this.etaskId = etaskId;
	}
}

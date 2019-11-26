package com.simon.margaret.app;

/**
 * Created by apple on 2019/11/19.
 */


public class UserInfo {
	private String name;
	private String account;
	private String uid;
	private String hid;
	private String mobile;
	private String password;
	private String sightStandard;
	private String token;

	public UserInfo() {
	}

	public UserInfo(String name, String account, String uid, String hid, String mobile, String password, String sightStandard, String token) {
		this.name = name;
		this.account = account;
		this.uid = uid;
		this.hid = hid;
		this.mobile = mobile;
		this.password = password;
		this.sightStandard = sightStandard;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSightStandard() {
		return sightStandard;
	}

	public void setSightStandard(String sightStandard) {
		this.sightStandard = sightStandard;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

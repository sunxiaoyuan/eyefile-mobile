package com.simon.margaret.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.simon.margaret.activities.ProxyActivity;
import com.simon.margaret.util.storage.MargaretPreference;

import java.util.HashMap;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by apple on 2019/8/22.
 */

public class Margaret {

	public static Configurator init(Context context) {
		Configurator.getInstance()
				.getPeanutConfigs()
				.put(ConfigKeys.APPLICATION_CONTEXT,
						context.getApplicationContext());
		return Configurator.getInstance();
	}

	public static Configurator getConfigurator() {
		return Configurator.getInstance();
	}

	public static <T> T getConfiguration(Enum<ConfigKeys> key) {
		return getConfigurator().getConfiguration(key);
	}

	public static Application getApplicationContext() {
		return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
	}

	public static Handler getHandler() {
		return getConfiguration(ConfigKeys.HANDLER);
	}


	public static void saveCurrentUser(UserInfo info) {
		String bean = JSON.toJSONString(info);
		MargaretPreference.addCustomAppProfile(ConfigKeys.CURRENT_USER.name(), bean);
	}

	public static UserInfo getCurrentUser() {
		String infoStr = MargaretPreference.getCustomAppProfile(ConfigKeys.CURRENT_USER.name());
		if (infoStr != null && !"".equalsIgnoreCase(infoStr)) {
			return JSON.parseObject(infoStr, UserInfo.class);
		}
		return null;
	}

	public static void eraseLocalUserInfo() {
		MargaretPreference.addCustomAppProfile(ConfigKeys.CURRENT_USER.name(), null);
	}
}

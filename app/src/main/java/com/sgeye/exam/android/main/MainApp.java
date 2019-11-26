package com.sgeye.exam.android.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.sgeye.exam.android.tasks.InitAppConfigTask;
import com.sgeye.exam.android.tasks.InitWebSocketClientTask;
import com.simon.margaret.util.launchstarter.TaskDispatcher;


/**
 * Created by apple on 2019/8/22.
 */
// 测试账号 hlcs 761875
public class MainApp extends Application implements Application.ActivityLifecycleCallbacks {

	@Override
	public void onCreate() {
		super.onCreate();

		// 使用启动器，进行App初始化配置
		TaskDispatcher.init(MainApp.this);
		TaskDispatcher dispatcher = TaskDispatcher.createInstance();

		dispatcher.addTask(new InitAppConfigTask())
				.addTask(new InitWebSocketClientTask())
				.start();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {
	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}
}

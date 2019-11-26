package com.sgeye.exam.android.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.githang.statusbar.StatusBarCompat;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.modules.bottom.BottomDelegate;
import com.sgeye.exam.android.modules.signin.SignInHyberDelegate;
import com.simon.margaret.activities.ProxyActivity;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.IUserChecker;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import org.java_websocket.framing.Framedata;

import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends ProxyActivity implements SocketListener {

	private static MargaretDelegate FIRST_DELEGATE = null;

	//定义一个变量，来标识是否退出
	private static boolean isExit = false;

	@Override
	public MargaretDelegate setRootDelegate() {

		// 检查用户是否登录了APP
		AccountManager.checkAccount(new IUserChecker() {
			@Override
			public void onSignIn() {
				// 已经登录
				FIRST_DELEGATE = new BottomDelegate();
			}

			@Override
			public void onNotSignIn() {
				// 没有登录
				FIRST_DELEGATE = new SignInHyberDelegate();
			}
		});

		return FIRST_DELEGATE;

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// 连接socket
		starSocket();

		// Window级别开启硬件加速
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		// 设置方向
		if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}

		Margaret.getConfigurator().withActivity(this);

		// 设置透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			StatusBarCompat.setStatusBarColor(
					this, Margaret.getApplicationContext().getColor(R.color.white));
		}

		// 动态申请权限
		requestPermissions();
	}

	@Override
	protected void onDestroy() {
		// 页面销毁时，停止连接，注销监听
		WebSocketHandler.getDefault().disConnect();
		WebSocketHandler.getDefault().removeListener(this);
		super.onDestroy();
	}

	private void requestPermissions() {
		Dexter.withActivity(this)
				.withPermissions(
						Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE
				).withListener(new MultiplePermissionsListener() {
			@Override
			public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

			@Override
			public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
		}).check();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			exit();
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	private void exit() {
//		if (!isExit) {
//			isExit = true;
//			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//			//利用handler延迟发送更改状态信息
//			handler.sendEmptyMessageDelayed(0, 2000);
//		} else {
//			finish();
//			System.exit(0);
//		}
//	}

	// -------------------  socket 生命周期回调 ------------------- //

	private void starSocket() {

		String ip = MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name());
		if (!"".equals(ip)) {
			// 启动连接
			WebSocketHandler.getDefault().start();
			// ip写入本地文件
			MargaretPreference.addCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name(), ip);
		} else {
			// 之前从来没有连接过
			ToastUtils.showShort("暂时没有可连接的Pad");
		}
		// 这一步必须设置
		WebSocketHandler.getDefault().addListener(this);
	}


	@Override
	public void onConnected() {
		String ip = MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name());
		if (!"".equals(ip)) {
			ToastUtils.showShort("连接 " + ip + " 成功");
		}
		ObserverManager.getInstance().notifyObserver("onConnected:true");
	}

	@Override
	public void onConnectFailed(Throwable e) {
		String ip = MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name());
		if (!"".equals(ip)) {
			ToastUtils.showShort("连接 " + ip + " 失败");
		}
		ObserverManager.getInstance().notifyObserver("onConnected:false");
	}

	@Override
	public void onDisconnect() {
		String ip = MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name());
		if (!"".equals(ip)) {
			ToastUtils.showShort("与 " + ip + " 连接断开");
		}
		ObserverManager.getInstance().notifyObserver("onConnected:false");
	}

	@Override
	public void onSendDataError(ErrorResponse errorResponse) {

	}

	@Override
	public <T> void onMessage(String message, T data) {
		// 接收pad信息
		if (!StringUtils.isEmpty(message)) {
			String[] split = message.split(":");
			if ("control".equals(split[0])) {  // 这是pad换行的消息
				@SuppressWarnings("unchecked")				final IGlobalCallback<String> callback = CallbackManager
						.getInstance()
						.getCallback(CallbackType.ON_MESSAGE_CHANGE_LINE);
				if (callback != null) {
					callback.executeCallback(split[1]);
				}
			} else if ("result".equals(split[0])) { // 这是检查结果的消息
				@SuppressWarnings("unchecked")				final IGlobalCallback<String> callback = CallbackManager
						.getInstance()
						.getCallback(CallbackType.ON_MESSAGE_RESULT);
				if (callback != null) {
					callback.executeCallback(split[1]);
				}
			}
		}
	}

	@Override
	public <T> void onMessage(ByteBuffer bytes, T data) {

	}

	@Override
	public void onPing(Framedata framedata) {

	}

	@Override
	public void onPong(Framedata framedata) {

	}

	@Override
	public void post(Runnable runnable) {

	}
}

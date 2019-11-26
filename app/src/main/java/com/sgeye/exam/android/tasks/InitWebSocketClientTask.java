package com.sgeye.exam.android.tasks;

import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.util.launchstarter.task.MainTask;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

/**
 * Created by apple on 2019/11/19.
 */

public class InitWebSocketClientTask extends MainTask {

	@Override
	public void run() {

		initWebSocket();
	}

	private void initWebSocket() {
		WebSocketSetting setting = new WebSocketSetting();
		//连接地址，必填，例如 wss://echo.websocket.org

		String ip = MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name());
		if (!"".equals(ip)) {
			setting.setConnectUrl(ip); //必填
		} else {
//			setting.setConnectUrl("ws://192.168.10.10:10086");
		}
		// 设置连接超时时间
		setting.setConnectTimeout(15 * 1000);

		// 设置心跳间隔时间
		setting.setConnectionLostTimeout(60);

		// 设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
		setting.setReconnectFrequency(3);

		// 网络状态发生变化后是否重连，
		// 需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
		setting.setReconnectWithNetworkChanged(false);

		// 通过 init 方法初始化默认的 WebSocketManager 对象
		WebSocketManager manager = WebSocketHandler.init(setting);
//			WebSocketHandler.registerNetworkChangedReceiver(Margaret.getApplicationContext());


	}
}

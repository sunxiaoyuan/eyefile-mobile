package com.sgeye.exam.android.modules.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.modules.scanner.ScannerDelegate;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.observer.ObserverListener;
import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.simon.margaret.util.checker.RegxUtil;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketSetting;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by apple on 2019/9/4.
 */

public class ConDeviceDelegate extends MargaretDelegate implements ObserverListener{

	private final static int CHOOSE_QR = 0;
	private final static int CHOOSE_MANUAL = 1;
	private final int mChooseColor = Margaret.getApplicationContext().getResources().getColor(R.color.mainColor);
	private final int mUnChooseColor = Margaret.getApplicationContext().getResources().getColor(R.color.chooseGray);

	@BindView(R2.id.ll_conn_method_qr)
	LinearLayout connMethodQRLL;
	@BindView(R2.id.ll_conn_method_manual)
	LinearLayout connMethodManualLL;

	@BindView(R2.id.et_conn_manual)
	EditText connManualET;

	@BindView(R2.id.tv_choose_qr)
	TextView chooseQRTV;
	@BindView(R2.id.v_choose_qr)
	View chooseQRView;
	@BindView(R2.id.tv_choose_manual)
	TextView chooseManualTV;
	@BindView(R2.id.v_choose_manual)
	View chooseManualView;


	// 连接状态
	@BindView(R2.id.tv_conn_connected)
	TextView connConnectedTV;
	// 未连接 - 面板
	@BindView(R2.id.tv_conn_stat_off)
	TextView connOffPanel;
	// 未连接 - 面板
	@BindView(R2.id.ll_conn_stat_on)
	RelativeLayout connOnPanel;
	// 设备ip
	@BindView(R2.id.tv_conn_device)
	TextView connDeviceTV;


	@Override
	public Object setLayout() {
		return R.layout.delegate_conn_device;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {


		ObserverManager.getInstance().add(this);

		initView();

		// 处理二维码扫描回调
		CallbackManager.getInstance().addCallback(CallbackType.ON_CONN_DEVICE_SCAN, new IGlobalCallback<String>() {
			@Override
			public void executeCallback(@Nullable String args) {
				if (!"".equals(args)) {
					connectSocketServer(args);
				}
			}
		});

		updateUIWithSocketConnectResult();
	}
	@Override
	public void observerUpData(Object msg) {
		String note = (String) msg;
		String[] split = note.split(":");
		if (split[0].equals("onConnected")) {
			updateUIWithSocketConnectResult();
		}
	}

	private void updateUIWithSocketConnectResult() {
		if (WebSocketHandler.getDefault().isConnect()) {
			// on
			connOnPanel.setAlpha(1);
			connDeviceTV.setText(MargaretPreference.getCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name()));
			connOffPanel.setAlpha(0);
		} else {
			// off
			connOnPanel.setAlpha(0);
			connOffPanel.setAlpha(1);
		}
	}

	private void initView() {
		// 初始化tab bar样式
		setConnMethodPanel(CHOOSE_QR);
		connManualET.setHint("请输入IP地址");
		connManualET.setHintTextColor(Color.parseColor("#999999"));
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		ObserverManager.getInstance().remove(this);
		super.onDestroy();
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return new DefaultHorizontalAnimator();
	}

	// 返回按钮
	@OnClick(R2.id.btn_conn_back)
	public void back() {
		getSupportDelegate().pop();
	}

	// 切换扫码/手动
	@OnClick(R2.id.ll_choose_qr)
	public void chooseQrConn() {
		setConnMethodPanel(CHOOSE_QR);
	}

	@OnClick(R2.id.ll_choose_manual)
	public void chooseManualConn() {
		setConnMethodPanel(CHOOSE_MANUAL);
	}

	private void setConnMethodPanel(int code) {
		switch (code) {
			case CHOOSE_QR:
				connMethodManualLL.setVisibility(View.GONE);
				connMethodQRLL.setVisibility(View.VISIBLE);
				chooseQRTV.setTextColor(mChooseColor);
				chooseQRView.setBackgroundColor(mChooseColor);
				chooseManualTV.setTextColor(mUnChooseColor);
				chooseManualView.setBackgroundColor(mUnChooseColor);
				break;
			case CHOOSE_MANUAL:
				connMethodQRLL.setVisibility(View.GONE);
				connMethodManualLL.setVisibility(View.VISIBLE);
				chooseManualTV.setTextColor(mChooseColor);
				chooseManualView.setBackgroundColor(mChooseColor);
				chooseQRTV.setTextColor(mUnChooseColor);
				chooseQRView.setBackgroundColor(mUnChooseColor);
				break;
			default:
				break;
		}
	}

	// 点击手动链接按钮
	@OnClick(R2.id.btn_conn_manual)
	public void connManual() {
		// 校验格式
		if (RegxUtil.checkIPWithEditText(connManualET)) {
			String ip = connManualET.getText().toString().trim();
			connectSocketServer(ip);
		}
	}

	// 点击二维码连接按钮
	@OnClick(R2.id.btn_conn_qr)
	public void connQrcode() {
		final ScannerDelegate delegate = new ScannerDelegate();
		delegate.setCallbackType(CallbackType.ON_CONN_DEVICE_SCAN);
		start(delegate);
	}

	// 断开连接
	@OnClick(R2.id.tv_conn_disconnect)
	public void disconnect() {
		WebSocketHandler.getDefault().disConnect();
	}

	private void connectSocketServer(String ip) {
		StringBuffer curIp = new StringBuffer("ws://");
		curIp.append(ip).append(":10086");
		// 更新ip
		WebSocketSetting setting = WebSocketHandler.getDefault().getSetting();
		setting.setConnectUrl(curIp.toString());
		// 重连
		WebSocketHandler.getDefault().reconnect(setting);
		// ip写入本地文件
		MargaretPreference.addCustomAppProfile(ConfigKeys.SOCKET_SERVER_IP.name(), curIp.toString());
	}


}

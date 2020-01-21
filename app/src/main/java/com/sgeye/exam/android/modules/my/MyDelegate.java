package com.sgeye.exam.android.modules.my;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.blutooth_printer.BluetoothDeviceListDelegate;
import com.sgeye.exam.android.blutooth_printer.CheckPrinter;
import com.sgeye.exam.android.blutooth_printer.DeviceConnFactoryManager;
import com.sgeye.exam.android.blutooth_printer.PrinterCommand;
import com.sgeye.exam.android.blutooth_printer.ThreadPool;
import com.sgeye.exam.android.modules.bottom.BottomDelegate;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.sgeye.exam.android.modules.check.ResultDialog;
import com.sgeye.exam.android.modules.signin.SignInHyberDelegate;
import com.sgeye.exam.android.utils.SoundUtils;
import com.simon.margaret.app.AccountManager;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.app.UserInfo;
import com.simon.margaret.delegates.MargaretDelegate;
import com.simon.margaret.observer.ObserverListener;
import com.simon.margaret.observer.ObserverManager;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.storage.MargaretPreference;
import com.zhangke.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;

import com.gprinter.command.CpclCommand;
import com.gprinter.command.EscCommand;
import com.gprinter.command.FactoryCommand;
import com.gprinter.command.LabelCommand;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.sgeye.exam.android.blutooth_printer.Constant.ACTION_USB_PERMISSION;
import static com.sgeye.exam.android.blutooth_printer.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.sgeye.exam.android.blutooth_printer.DeviceConnFactoryManager.CONN_STATE_FAILED;
import static com.simon.margaret.app.Margaret.getApplicationContext;

/**
 * Created by apple on 2019/9/4.
 */

public class MyDelegate extends BottomItemDelegate
		implements ObserverListener, OnChangeRefrection, OnChangePrintSettings, OnChangeOptometry {

	private int mRefrectionIndex = 0;
	private int mPrintSettingsIndex = 0;
	private int mOptometryIndex = 0;
	private List<String> mRefractionList = Arrays.asList("伟伦", "索维", "莫廷", "其他");
	private List<String> mPrintSettingList = Arrays.asList("不打印", "打印带回执", "打印不带回执");
	private List<String> mOptometryList = Arrays.asList("拓普康", "索维", "尼德克",
			"天乐", "新缘", "法里奥", "其他");

	@BindView(R2.id.tv_my_method)
	TextView methodTV;
	@BindView(R2.id.tv_my_name)
	TextView nameTV;
	@BindView(R2.id.tv_my_account)
	TextView accountTV;
	@BindView(R2.id.tv_my_phone)
	TextView phoneTV;

	@BindView(R2.id.ll_list_device)
	LinearLayout connDeviceLL;
	@BindView(R2.id.ll_list2)
	LinearLayout countMethodLL;
	@BindView(R2.id.tv_my_conStat)
	TextView myConStatTV;

	@BindView(R2.id.tv_refraction_check)
	TextView tv_refraction_check;

	@BindView(R2.id.tv_print_settings)
	TextView tv_print_settings;

	@BindView(R2.id.tv_optometry)
	TextView tv_optometry;


	@BindView(R2.id.tv_my_bluetooth_state)
	TextView tv_my_bluetooth_state;

	// ---- 打印相关 ---- //
	private int counts;
	ArrayList<String> per = new ArrayList<>();
	private UsbManager usbManager;
	// 判断打印机所使用指令是否是ESC指令
	private int mid = 0;
	private ThreadPool threadPool;
	private static final int REQUEST_CODE = 0x004;
	/**
	 * 连接状态断开
	 */
	private static final int CONN_STATE_DISCONN = 0x007;

	/**
	 * 使用打印机指令错误
	 */
	private static final int PRINTER_COMMAND_ERROR = 0x008;

	/**
	 * ESC查询打印机实时状态指令
	 */
	private byte[] esc = {0x10, 0x04, 0x02};

	/**
	 * CPCL查询打印机实时状态指令
	 */
	private byte[] cpcl = {0x1b, 0x68};

	/**
	 * TSC查询打印机状态指令
	 */
	private byte[] tsc = {0x1b, '!', '?'};

	private static final int CONN_MOST_DEVICES = 0x11;
	private static final int CONN_PRINTER = 0x12;
	private int printcount = 0;
	private boolean continuityprint = false;


	@Override
	public Object setLayout() {
		return R.layout.delegate_my;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

		ObserverManager.getInstance().add(this);

		if (WebSocketHandler.getDefault().isConnect()) {
			myConStatTV.setText("已连接");
		} else {
			myConStatTV.setText("未连接");
		}

		handleConnectBlutoothDevice();
	}


	@Override
	public void onDestroy() {
		ObserverManager.getInstance().remove(this);
		super.onDestroy();
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);

		UserInfo currentUser = Margaret.getCurrentUser();
		if (currentUser != null) {
			String name = currentUser.getName();
			String phone = currentUser.getMobile();
			String account = currentUser.getAccount();
			String method = currentUser.getSightStandard();
			if (!StringUtils.isEmpty(name)) {
				nameTV.setText(name);
			}
			if (!StringUtils.isEmpty(phone)) {
				phoneTV.setText(phone);
			}
			if (!StringUtils.isEmpty(account)) {
				accountTV.setText(account);
			}
			if ("0".equals(method)) {
				methodTV.setText("对数记数法");
			} else {
				methodTV.setText("小数记数法");
			}
		}
	}

	@Override
	public void observerUpData(Object msg) {
		String note = (String) msg;
		String[] split = note.split(":");
		if (split[0].equals("onConnected")) {
			if ("true".equals(split[1])) {
				// on
				myConStatTV.setText("已连接");
			} else {
				// off
				myConStatTV.setText("未连接");
			}
		}
	}

	// 点击设备连接，页面跳转
	@OnClick(R2.id.ll_list_device)
	public void jump2ConnDevicePage() {
		final BottomDelegate bottomDelegate = getParentDelegate();
		MargaretDelegate connDevicePage = new ConDeviceDelegate();
		bottomDelegate.start(connDevicePage);
	}

	// 点击视力计数法，页面跳转
	@OnClick(R2.id.ll_list2)
	public void jump2CountMethodPage() {
		ToastUtils.showLong("视力计数法不允许修改");
	}

	// 退出登录
	@OnClick(R2.id.btn_my_logout)
	public void logout() {
		// 清除本地信息
//        AccountManager.setSignState(false);
//
//        Margaret.eraseLocalUserInfo();
//        // 清除cookie
//        MargaretPreference.addCustomAppProfile("cookie", null);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
//                @Override
//                public void onReceiveValue(Boolean value) {
//
//                }
//            });
//        }
//        // 跳转到登录页
//        getParentDelegate().getSupportDelegate().startWithPop(new SignInHyberDelegate());

//		SoundUtils.playSound();

		btnLabelPrint();
	}

	@OnClick(R2.id.ll_list_quguang)
	public void changeRefractionCheck() {
		RefractionCheckDialog.Builder builder = new RefractionCheckDialog.Builder(getContext());
		builder.onChangeRefrection(this).distance(mRefrectionIndex).create().show();
	}

	@Override
	public void onChangeRefrection(int index) {
		mRefrectionIndex = index;
		tv_refraction_check.setText(mRefractionList.get(index));
	}

	@OnClick(R2.id.ll_list_print_settings)
	public void changePrintSettings() {
		PrintSettingsDialog.Builder builder = new PrintSettingsDialog.Builder(getContext());
		builder.onChangePrintSettings(this).distance(mPrintSettingsIndex).create().show();
	}

	@Override
	public void onChangePrintSettings(int index) {
		// 0:不打印 1:打印带回执 2:打印不带回执
		mPrintSettingsIndex = index;
		tv_print_settings.setText(mPrintSettingList.get(index));
		// 保存到本地设置
		MargaretPreference.addCustomAppProfile("KEY_PRINT_SETTING", String.valueOf(mPrintSettingsIndex));
	}

	@OnClick(R2.id.ll_list_pc_yanguang)
	public void changeOptometry() {
		OptometryDialog.Builder builder = new OptometryDialog.Builder(getContext());
		builder.onChangeOptometry(this).distance(mOptometryIndex).create().show();
	}

	@Override
	public void onChangeOptometry(int index) {
		mOptometryIndex = index;
		tv_optometry.setText(mOptometryList.get(index));
	}

	@OnClick(R2.id.ll_list_bluetoothe_printer)
	public void connectBlutoothPrinter() {
		getParentDelegate().getSupportDelegate().start(new BluetoothDeviceListDelegate());
	}

	// 处理蓝牙设备回调
	private void handleConnectBlutoothDevice() {
		CallbackManager.getInstance().addCallback(CallbackType.ON_CLICK_BLUTOOTH_DEVICE, args -> {
			String address = (String) args;
//			closeport();
			/* 初始化话DeviceConnFactoryManager */
			new DeviceConnFactoryManager.Build()
					.setId(mid)
					/* 设置连接方式 */
					.setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
					/* 设置连接的蓝牙mac地址 */
					.setMacAddress(address)
					.build();
			/* 打开端口 */
			Log.d("MyDelegate", "onActivityResult: 连接蓝牙" + mid);

			threadPool = ThreadPool.getInstantiation();
			threadPool.addTask(new Runnable() {
				@Override
				public void run() {
					DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].openPort();
				}
			});
		});
	}


	// 重新连接回收上次连接的对象，避免内存泄漏
	private void closeport() {
		if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid] != null && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].mPort != null) {
//            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].reader.cancel();
			DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].mPort.closePort();
			DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].mPort = null;
		}
	}

	/**
	 * 打印机状态查询
	 *
	 * @param view
	 */
	public void btnPrinterState(View view) {
		/* 打印机状态查询 */
		if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid] == null ||
				!DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getConnState()) {
			ToastUtils.showShort(getString(R.string.str_cann_printer));
			return;
		}
		DeviceConnFactoryManager.whichFlag = true;
		ThreadPool.getInstantiation().addTask(new Runnable() {
			@Override
			public void run() {
				Vector<Byte> data = new Vector<>(esc.length);
				if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getCurrentPrinterCommand() == PrinterCommand.ESC) {
					for (int i = 0; i < esc.length; i++) {
						data.add(esc[i]);
					}
					DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].sendDataImmediately(data);
				} else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getCurrentPrinterCommand() == PrinterCommand.TSC) {
					for (int i = 0; i < tsc.length; i++) {
						data.add(tsc[i]);
					}
					DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].sendDataImmediately(data);
				} else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getCurrentPrinterCommand() == PrinterCommand.CPCL) {
					for (int i = 0; i < cpcl.length; i++) {
						data.add(cpcl[i]);
					}
					DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].sendDataImmediately(data);
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(ACTION_USB_DEVICE_DETACHED);
		filter.addAction(ACTION_QUERY_PRINTER_STATE);
		filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
		filter.addAction(ACTION_USB_DEVICE_ATTACHED);
		_mActivity.registerReceiver(receiver, filter);
	}

	@Override
	public void onStop() {
		super.onStop();
		_mActivity.unregisterReceiver(receiver);
	}


	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			switch (action) {
				/* Usb连接断开、蓝牙连接断开广播 */
				case ACTION_USB_DEVICE_DETACHED:
					mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
					break;
				case DeviceConnFactoryManager.ACTION_CONN_STATE:
					int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
					int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
					switch (state) {
						case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
							if (mid == deviceId) {
								tv_my_bluetooth_state.setText("未连接");
							}
							break;
						case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
							tv_my_bluetooth_state.setText("连接中");
							break;
						case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
							tv_my_bluetooth_state.setText("已连接");
							break;
						case CONN_STATE_FAILED:
							ToastUtils.showShort(getString(R.string.str_conn_fail));
							tv_my_bluetooth_state.setText("未连接");
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CONN_STATE_DISCONN:
					if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid] != null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getConnState()) {
						DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].closePort(mid);
						ToastUtils.showShort(getString(R.string.str_disconnect_success));
					}
					break;
				case PRINTER_COMMAND_ERROR:
					ToastUtils.showShort(getString(R.string.str_choice_printer_command));
					break;
				case CONN_PRINTER:
					ToastUtils.showShort(getString(R.string.str_cann_printer));
					break;
			}
		}
	};

	public void btnLabelPrint() {
		if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid] == null ||
				!DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getConnState()) {
			ToastUtils.showShort(getString(R.string.str_cann_printer));
			return;
		}
		threadPool = ThreadPool.getInstantiation();
		threadPool.addTask(new Runnable() {
			@Override
			public void run() {
				if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[mid].getCurrentPrinterCommand() == PrinterCommand.ESC) {
					CheckPrinter.receiptPrint(true);
				} else {
					mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
				}
			}
		});
	}


}

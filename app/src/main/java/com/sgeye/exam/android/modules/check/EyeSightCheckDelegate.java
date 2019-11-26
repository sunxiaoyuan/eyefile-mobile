package com.sgeye.exam.android.modules.check;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.camera.bean.CheckPadEventBean;
import com.sgeye.exam.android.constants.AppConstants;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.sgeye.exam.android.modules.check.list.ControlAdapter;
import com.sgeye.exam.android.modules.check.list.ControlClickListener;
import com.sgeye.exam.android.modules.check.list.ControlConverter;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.net.RestClient;
import com.simon.margaret.net.callback.IError;
import com.simon.margaret.net.callback.IFailure;
import com.simon.margaret.net.callback.ISuccess;
import com.simon.margaret.ui.recycler.MultipleItemEntity;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.zhangke.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by apple on 2019/11/20.
 */

public class EyeSightCheckDelegate extends BottomItemDelegate implements OnChangeDistance {

	private CheckPadEventBean mBean;

	private static final int CHECK_STAT_GOING = 0;
	private static final String CHECK_STAT_GOING_COLOR = "#ffffbb33";

	private static final int CHECK_STAT_DONE = 1;
	private static final String CHECK_STAT_DONE_COLOR = "#00A97E";

	private static final int CHECK_STAT_NOT = 2;
	private static final String CHECK_STAT_NOT_COLOR = "#ACACAC";

	// 默认先检查近距离 2 or 5
	private int mDistance = 5;

	// 视力检查结果
	private String ucvaOd = ""; // 远-右-裸
	private String ucvaOs = ""; // 远-左-裸
	private String cvaOd = ""; // 远-右-镜
	private String cvaOs = ""; // 远-左-镜

	private String ucvaNearOd = ""; // 近-右-裸
	private String ucvaNearOs = ""; // 近-左-裸
	private String cvaNearOd = ""; // 近-右-镜
	private String cvaNearOs = ""; // 近-左-镜

	private ControlConverter mConverter = new ControlConverter();
	private ControlAdapter mAdapter;
	private int mCurIndex = 0;
	private int mCurCheck = 0;

	@BindView(R2.id.tv_check_right_nake)
	TextView rightNakeTV01;
	@BindView(R2.id.tv_check_left_nake)
	TextView leftNakeTV02;
	@BindView(R2.id.tv_check_right_glass)
	TextView rightGlassTV03;
	@BindView(R2.id.tv_check_left_glass)
	TextView leftGlassTV04;

	@BindView(R2.id.btn_check_right)
	Button checkRightButton;
	@BindView(R2.id.btn_check_center)
	Button checkCenterButton;
	@BindView(R2.id.btn_check_top)
	Button checkTopButton;
	@BindView(R2.id.btn_check_down)
	Button checkDownButton;
	@BindView(R2.id.btn_check_left)
	Button checkLeftButton;

	@BindView(R2.id.toolbar_check)
	Toolbar toolbar;

	@BindView(R2.id.rv_check_control)
	RecyclerView controlRecyclerView;

	@Override
	public Object setLayout() {
		return R.layout.delegate_eyesight_check;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
		initCheckResult();
		initList();
		// 处理pad发来的换行通知
		// handlePadMessageOfChangeLine();
		// 行数变更回调
		handleUserChangeLine();
		// 拿到测试结果回调
		handlePadMessageOfResult();
	}

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
	}

	public void setBean(CheckPadEventBean mBean) {
		this.mBean = mBean;
	}

	private void initCheckResult() {
		// 第一个设置为测量中，其他设置为未测量
		setCheckResultTVStat(rightNakeTV01, CHECK_STAT_GOING, "");
		setCheckResultTVStat(leftNakeTV02, CHECK_STAT_NOT, "");
		setCheckResultTVStat(rightGlassTV03, CHECK_STAT_NOT, "");
		setCheckResultTVStat(leftGlassTV04, CHECK_STAT_NOT, "");
	}

	private void setCheckResultTVStat(TextView textView, int stat, String result) {
		switch (stat) {
			case CHECK_STAT_GOING:
				textView.setText("测量中");
				textView.setTextColor(Color.parseColor(CHECK_STAT_GOING_COLOR));
				break;
			case CHECK_STAT_DONE:
				textView.setText(result);
				textView.setTextColor(Color.parseColor(CHECK_STAT_DONE_COLOR));
				break;
			case CHECK_STAT_NOT:
				textView.setText("未测量");
				textView.setTextColor(Color.parseColor(CHECK_STAT_NOT_COLOR));
				break;
			default:
				break;
		}
	}

	private void handlePadMessageOfResult() {
		CallbackManager.getInstance().addCallback(CallbackType.ON_MESSAGE_RESULT, args -> {
			String ret = (String) args;

			if (mCurCheck <= 3) {
				// 测试尚未完成，拿到结果，再界面上展示
				updateCheckResult(mCurCheck, ret);
				if (mCurCheck == 3) {
					Toast.makeText(Margaret.getApplicationContext(),
							mDistance == 2 ? "近视力检查完毕" : "远视力检查完毕",
							Toast.LENGTH_SHORT).show();
					mCurCheck = 0;
				}
				mCurCheck++;
			}
			// 通知pad清空挑战记录
			WebSocketHandler.getDefault().send("clear:1");
		});
	}

	// 控制方向选择按钮是否禁用
	private void enableDirectionBtn(boolean enable) {
		checkRightButton.setEnabled(enable);
		checkCenterButton.setEnabled(enable);
		checkTopButton.setEnabled(enable);
		checkDownButton.setEnabled(enable);
		checkLeftButton.setEnabled(enable);
	}

	private void updateCheckResult(int mCurCheck, String result) {
		if (mCurCheck == 0) {
			setCheckResultTVStat(rightNakeTV01, CHECK_STAT_DONE, result);
			setCheckResultTVStat(leftNakeTV02, CHECK_STAT_GOING, result);
			Toast.makeText(Margaret.getApplicationContext(),
					mDistance == 2 ? "右眼裸眼近视力检查完毕" : "右眼裸眼远视力检查完毕",
					Toast.LENGTH_SHORT).show();
			if (mDistance == 2) {
				ucvaNearOd = result;
			} else {
				ucvaOd = result;
			}
		} else if (mCurCheck == 1) {
			setCheckResultTVStat(leftNakeTV02, CHECK_STAT_DONE, result);
			setCheckResultTVStat(rightGlassTV03, CHECK_STAT_GOING, result);
			Toast.makeText(Margaret.getApplicationContext(),
					mDistance == 2 ? "左眼裸眼近视力检查完毕" : "左眼裸眼远视力检查完毕",
					Toast.LENGTH_SHORT).show();
			if (mDistance == 2) {
				ucvaNearOs = result;
			} else {
				ucvaOs = result;
			}
		} else if (mCurCheck == 2) {
			setCheckResultTVStat(rightGlassTV03, CHECK_STAT_DONE, result);
			setCheckResultTVStat(leftGlassTV04, CHECK_STAT_GOING, result);
			Toast.makeText(Margaret.getApplicationContext(),
					mDistance == 2 ? "右眼戴镜近视力检查完毕" : "右眼戴镜远视力检查完毕",
					Toast.LENGTH_SHORT).show();
			if (mDistance == 2) {
				cvaNearOd = result;
			} else {
				cvaOd = result;
			}
		} else if (mCurCheck == 3) {
			setCheckResultTVStat(leftGlassTV04, CHECK_STAT_DONE, result);
			Toast.makeText(Margaret.getApplicationContext(),
					mDistance == 2 ? "左眼戴镜近视力检查完毕" : "左眼戴镜远视力检查完毕",
					Toast.LENGTH_SHORT).show();
			if (mDistance == 2) {
				cvaNearOs = result;
			} else {
				cvaOs = result;
			}
		}
	}

	// 将不同距离的检查结果展示出来
	private void showResult() {
		if (mDistance == 5) {
			setCheckResultTVStat(rightNakeTV01,
					StringUtils.isEmpty(ucvaOd) ? CHECK_STAT_GOING : CHECK_STAT_DONE, ucvaOd);
			setCheckResultTVStat(leftNakeTV02,
					StringUtils.isEmpty(ucvaOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, ucvaOs);
			setCheckResultTVStat(rightGlassTV03,
					StringUtils.isEmpty(cvaOd) ? CHECK_STAT_NOT : CHECK_STAT_DONE, cvaOd);
			setCheckResultTVStat(leftGlassTV04,
					StringUtils.isEmpty(cvaOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, cvaOs);
		} else if (mDistance == 2) {
			setCheckResultTVStat(rightNakeTV01,
					StringUtils.isEmpty(ucvaNearOd) ? CHECK_STAT_GOING : CHECK_STAT_DONE, ucvaNearOd);
			setCheckResultTVStat(leftNakeTV02,
					StringUtils.isEmpty(ucvaNearOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, ucvaNearOs);
			setCheckResultTVStat(rightGlassTV03,
					StringUtils.isEmpty(cvaNearOd) ? CHECK_STAT_NOT : CHECK_STAT_DONE, cvaNearOd);
			setCheckResultTVStat(leftGlassTV04,
					StringUtils.isEmpty(cvaNearOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, cvaNearOs);
		}
	}

	private void handleUserChangeLine() {
		CallbackManager.getInstance().addCallback(CallbackType.ON_CLICK, args -> {
			Integer index = (Integer) args;
			mCurIndex = index;
		});
	}

	private void handlePadMessageOfChangeLine() {
		CallbackManager.getInstance().addCallback(CallbackType.ON_MESSAGE_CHANGE_LINE, args -> {
			String command = (String) args;
			if (AppConstants.SOCKET_COMMAND_CHANGE_NEXT_LINE.equals(command)) {
				// 下一行
				mAdapter.onItemClick(mCurIndex + 1);
			} else if (AppConstants.SOCKET_COMMAND_CHANGE_LAST_LINE.equals(command)) {
				// 上一行
				mAdapter.onItemClick(mCurIndex - 1);
			}
		});
	}

	private void initList() {
		final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		controlRecyclerView.setLayoutManager(manager);

		ArrayList<MultipleItemEntity> itemEntities = mConverter.setJsonData("").convert();
		mAdapter = new ControlAdapter(itemEntities);
		controlRecyclerView.setAdapter(mAdapter);
		controlRecyclerView.addOnItemTouchListener(new ControlClickListener());
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return new DefaultHorizontalAnimator();
	}

	// 返回
	@OnClick(R2.id.btn_check_back)
	public void back() {
		getSupportDelegate().pop();
	}

	// 控制方向 - 上
	@OnClick(R2.id.btn_check_top)
	public void up() {
		WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_UP);
	}

	// 控制方向 - 下
	@OnClick(R2.id.btn_check_down)
	public void down() {
		WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_DOWN);
	}

	// 控制方向 - 左
	@OnClick(R2.id.btn_check_left)
	public void left() {
		WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_LEFT);
	}

	// 控制方向 - 右
	@OnClick(R2.id.btn_check_right)
	public void right() {
		WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_RIGHT);
	}

	// 看不清
	@OnClick(R2.id.btn_check_center)
	public void notClear() {
		WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_CENTER);
	}

	// 检测距离
	@OnClick(R2.id.btn_check_distance)
	public void changeDistance() {
		DistanceDialog.Builder builder = new DistanceDialog.Builder(getContext());
		builder.onChangeDistance(this).distance(mDistance).create().show();
	}

	// 选择距离的回调
	@Override
	public void onChangeDistance(int distance) {
		mDistance = distance;
		// 展示检查结果
		showResult();
		mCurCheck = 0;
		// 通知pad变换E的大小
		if (mDistance == 2) {
			// 变小
			WebSocketHandler.getDefault().send("size:false");
		} else {
			// 变大
			WebSocketHandler.getDefault().send("size:true");
		}
	}

	// 保存结果
	@OnClick(R2.id.btn_check_save)
	public void save() {
		// 上传信息，然后通知pad关闭检查页面
		WeakHashMap<String, Object> params = new WeakHashMap<>();
		params.put("studentId", mBean.getStudentId());
		params.put("etaskId", mBean.getEtaskId());
		params.put("ucvaOd", ucvaOd);
		params.put("ucvaOs", ucvaOs);
		params.put("cvaOd", cvaOd);
		params.put("cvaOs", cvaOs);
		params.put("ucvaNearOd", ucvaNearOd);
		params.put("ucvaNearOs", ucvaNearOs);
		params.put("cvaNearOd", cvaNearOd);
		params.put("cvaNearOs", cvaNearOs);
		// map -> json string
		String paramsStr = JSON.toJSONString(params);

		// 上传
		RestClient.builder()
				.raw(paramsStr)
				.url("http://api2.okjing.net/efile/app/visual")
				.success(response -> {
					JSONObject parseObj = JSON.parseObject(response);
					int code = (int) parseObj.get("code");
					if (code == 0) {
						ToastUtils.showShort("保存成功" + parseObj.get("message"));
						WebSocketHandler.getDefault().send("onOpen:false");
						// 返回检查列表页面
						getSupportDelegate().pop();
					} else {
						ToastUtils.showShort((String) parseObj.get("message"));
					}
				})
				.failure(() -> {

				})
				.error((code, msg) -> {

				})
				.build()
				.post();

	}
}

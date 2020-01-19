package com.sgeye.exam.android.modules.check;

import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sgeye.exam.android.R;
import com.sgeye.exam.android.R2;
import com.sgeye.exam.android.event.bean.CheckPadEventBean;
import com.sgeye.exam.android.constants.AppConstants;
import com.sgeye.exam.android.modules.bottom.BottomItemDelegate;
import com.sgeye.exam.android.modules.check.list.ControlAdapter;
import com.sgeye.exam.android.modules.check.list.ControlClickListener;
import com.sgeye.exam.android.modules.check.list.ControlConverter;
import com.simon.margaret.app.ConfigKeys;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.net.RestClient;
import com.simon.margaret.ui.loader.MargaretLoader;
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

public class EyeSightCheckDelegate extends BottomItemDelegate implements OnChangeDistance, ContinueDialogListener {

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
    private CheckResult checkResult = new CheckResult();


    private ControlConverter mConverter = new ControlConverter();
    private ControlAdapter mAdapter;
    private int mCurIndex = 9;
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

    ControlClickListener mControlClickListener = new ControlClickListener();

    @Override
    public Object setLayout() {
        return R.layout.delegate_eyesight_check;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initCheckResult();
        initList();
        // 处理pad发来的换行通知
        handlePadMessageOfChangeLine();
        // 行数变更回调
        handleUserChangeLine();
        // 拿到测试结果回调
        handlePadMessageOfResult();
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
            _mActivity.runOnUiThread(() -> {
                updateCheckResult(mCurCheck, ret);
                if (mCurCheck == 3) {
                    // 检查结束，不允许再测试
                    enableDirectionBtn(false);
                } else {
                    mCurCheck++;
                }
            });
            mControlClickListener.onItemClick(mAdapter, controlRecyclerView, 9);
            mCurIndex = 9;
            // 通知pad清空挑战记录，手机与pad都应该回到0.8
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
        playAudioTip();
        if (mCurCheck == 0) {
            setCheckResultTVStat(rightNakeTV01, CHECK_STAT_DONE, result);
            setCheckResultTVStat(leftNakeTV02, CHECK_STAT_GOING, result);
            showTipDialog("右眼裸眼视力检查完毕，请遮盖右眼，进行左眼裸眼视力检查");
            checkResult.ucvaOd = result;
        } else if (mCurCheck == 1) {
            setCheckResultTVStat(leftNakeTV02, CHECK_STAT_DONE, result);
            setCheckResultTVStat(rightGlassTV03, CHECK_STAT_GOING, result);
            checkResult.ucvaOs = result;

            ContinueDialog.Builder builder = new ContinueDialog.Builder(getContext());
            builder.setContinueDialogListener(this).create().show();

        } else if (mCurCheck == 2) {
            setCheckResultTVStat(rightGlassTV03, CHECK_STAT_DONE, result);
            setCheckResultTVStat(leftGlassTV04, CHECK_STAT_GOING, result);
            showTipDialog("右眼戴镜视力检查完毕，请遮盖右眼，进行左眼戴镜视力检查");
            checkResult.cvaOd = result;
        } else if (mCurCheck == 3) {
            // 直接弹出检查结果确认框
            setCheckResultTVStat(leftGlassTV04, CHECK_STAT_DONE, result);
            checkResult.cvaOs = result;
            ResultDialog.Builder builder = new ResultDialog.Builder(getContext());
            builder.checkResult(checkResult).create().show();
        }
    }

    // 将不同距离的检查结果展示出来
//    private void showResult() {
//        setCheckResultTVStat(rightNakeTV01,
//                StringUtils.isEmpty(checkResult.ucvaOd) ? CHECK_STAT_GOING : CHECK_STAT_DONE, checkResult.ucvaOd);
//        setCheckResultTVStat(leftNakeTV02,
//                StringUtils.isEmpty(checkResult.ucvaOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, checkResult.ucvaOs);
//        setCheckResultTVStat(rightGlassTV03,
//                StringUtils.isEmpty(checkResult.cvaOd) ? CHECK_STAT_NOT : CHECK_STAT_DONE, checkResult.cvaOd);
//        setCheckResultTVStat(leftGlassTV04,
//                StringUtils.isEmpty(checkResult.cvaOs) ? CHECK_STAT_NOT : CHECK_STAT_DONE, checkResult.cvaOs);
//
//    }

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
                mControlClickListener.onItemClick(mAdapter, controlRecyclerView, mCurIndex + 1);
            } else if (AppConstants.SOCKET_COMMAND_CHANGE_LAST_LINE.equals(command)) {
                // 上一行
                mControlClickListener.onItemClick(mAdapter, controlRecyclerView, mCurIndex - 1);
            }
        });
    }

    private void initList() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        controlRecyclerView.setLayoutManager(manager);

        ArrayList<MultipleItemEntity> itemEntities = mConverter.setJsonData("").convert();
        mAdapter = new ControlAdapter(itemEntities);
        controlRecyclerView.setAdapter(mAdapter);
        controlRecyclerView.addOnItemTouchListener(mControlClickListener);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    // 返回
    @OnClick(R2.id.btn_check_back)
    public void back() {
        WebSocketHandler.getDefault().send("onOpen:false");
        getSupportDelegate().pop();
    }

    // 控制方向 - 上
    @OnClick(R2.id.btn_check_top)
    public void up() {
        playAudioTip();
        WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_UP);
    }

    // 控制方向 - 下
    @OnClick(R2.id.btn_check_down)
    public void down() {
        playAudioTip();
        WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_DOWN);
    }

    // 控制方向 - 左
    @OnClick(R2.id.btn_check_left)
    public void left() {
        playAudioTip();
        WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_LEFT);
    }

    // 控制方向 - 右
    @OnClick(R2.id.btn_check_right)
    public void right() {
        playAudioTip();
        WebSocketHandler.getDefault().send(AppConstants.SOCKET_COMMAND_CONTROL_RIGHT);
    }

    // 看不清
    @OnClick(R2.id.btn_check_center)
    public void notClear() {
        playAudioTip();
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
//        showResult();
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
        params.put("ucvaOd", checkResult.ucvaOd);
        params.put("ucvaOs", checkResult.ucvaOs);
        params.put("cvaOd", checkResult.cvaOd);
        params.put("cvaOs", checkResult.cvaOs);
        // map -> json string
        String paramsStr = JSON.toJSONString(params);

        // 上传
        RestClient.builder()
                .raw(paramsStr)
                .url(Margaret.getConfiguration(ConfigKeys.API_HOST) + "/efile/app/visual")
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

    private void playAudioTip() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(Margaret.getApplicationContext(), uri);
        rt.play();
    }

    private void showTipDialog(String message) {
        TipDialog.Builder builder = new TipDialog.Builder(getContext());
        TipDialog tipDialog = builder.msg(message).create();
        tipDialog.show();
    }

    // 选择是否继续 - 是
    @Override
    public void onClickConfirm() {
        // do nothing
    }

    // 选择是否继续 - 否
    @Override
    public void onClickCancel() {
        // 禁止再继续测量
        enableDirectionBtn(false);
        // 更改ui
        setCheckResultTVStat(rightGlassTV03, CHECK_STAT_NOT, "");
        ResultDialog.Builder builder = new ResultDialog.Builder(getContext());
        builder.checkResult(checkResult).create().show();
    }
}

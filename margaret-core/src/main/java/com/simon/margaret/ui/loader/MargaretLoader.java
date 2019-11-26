package com.simon.margaret.ui.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.simon.margaret.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;
import com.wang.avi.indicators.BallClipRotateMultipleIndicator;
import com.wang.avi.indicators.PacmanIndicator;

import java.util.ArrayList;

/**
 * Created by apple on 2019/8/31.
 */

public class MargaretLoader {
    private static final int LOADER_SIZE_SCALE = 2;
    private static final int LOADER_OFFSET_SCALE = 10;
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    private static final Indicator DEFAULT_LOADER = new PacmanIndicator();

    private static AppCompatDialog createDialog(
            Context context, AVLoadingIndicatorView avLoadingIndicatorView) {

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        int deviceWidth = ScreenUtils.getScreenWidth();
        int deviceHeight = ScreenUtils.getScreenHeight();
        final Window dialogWindow = dialog.getWindow();
        dialog.setContentView(avLoadingIndicatorView);
        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        return dialog;
    }

    private static AppCompatDialog createTextDialog(
            Context context, View view, AVLoadingIndicatorView avLoadingIndicatorView) {

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        int deviceWidth = ScreenUtils.getScreenWidth();
        int deviceHeight = ScreenUtils.getScreenHeight();
        final Window dialogWindow = dialog.getWindow();
        avLoadingIndicatorView.smoothToShow();
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        return dialog;
    }

    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name());
    }

    public static void showLoading(Context context, String type) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        avLoadingIndicatorView.setIndicator(type);
        createDialog(context, avLoadingIndicatorView).show();
    }

    public static void showLoading(Context context, Indicator indicator) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        avLoadingIndicatorView.setIndicator(indicator);
        createDialog(context, avLoadingIndicatorView).show();
    }

    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    public static void showTextLoading(Context context, String msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);
        AVLoadingIndicatorView avLoadingIndicatorView =
                (AVLoadingIndicatorView) view.findViewById(R.id.AVLoadingIndicatorView);
        createTextDialog(context, view, avLoadingIndicatorView).show();
    }

    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        }
        LOADERS.clear();
    }

}

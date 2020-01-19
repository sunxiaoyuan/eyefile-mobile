package com.sgeye.exam.android.modules.check;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.sgeye.exam.android.R;

public class ResultDialog extends Dialog {

    public ResultDialog(Context context) {
        super(context);
    }

    public static class Builder {

        private Context context;
        private CheckResult checkResult;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder checkResult(CheckResult checkResult) {
            this.checkResult = checkResult;
            return this;
        }

        public ResultDialog create() {

            final ResultDialog dialog = new ResultDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_result, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView nakeRight = view.findViewById(R.id.tv_luoyan_right);
            TextView nakeLeft = view.findViewById(R.id.tv_luoyan_left);
            TextView adjustRight = view.findViewById(R.id.tv_jiaozheng_right);
            TextView adjustLeft = view.findViewById(R.id.tv_jiaozheng_left);

            nakeRight.setText(checkResult.ucvaOd);
            nakeLeft.setText(checkResult.ucvaOs);

            adjustRight.setText(StringUtils.isEmpty(checkResult.cvaOd)  ? "-" : checkResult.cvaOd);
            adjustLeft.setText(StringUtils.isEmpty(checkResult.cvaOs) ? "-" : checkResult.cvaOs);

            Button confirmBtn = view.findViewById(R.id.btn_result_confirm);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            return dialog;
        }

    }
}
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

import com.sgeye.exam.android.R;

interface ContinueDialogListener {
    void onClickConfirm();

    void onClickCancel();
}


// 视频查看弹窗
public class ContinueDialog extends Dialog {

    public ContinueDialog(Context context) {
        super(context);
    }

    public static class Builder {

        private Context context;
        private ContinueDialogListener listener;

        public Builder(Context context) {

            this.context = context;
        }

        public Builder setContinueDialogListener(ContinueDialogListener listener) {
            this.listener = listener;
            return this;
        }

        public ContinueDialog create() {

            final ContinueDialog dialog = new ContinueDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_continue, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button cancel = view.findViewById(R.id.btn_cancel_continue);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    listener.onClickCancel();
                }
            });

            Button confirm = view.findViewById(R.id.btn_confirm_continue);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    listener.onClickConfirm();
                }
            });

            return dialog;
        }

    }
}
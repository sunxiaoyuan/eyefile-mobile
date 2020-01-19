package com.sgeye.exam.android.modules.check;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sgeye.exam.android.R;


// 视频查看弹窗
public class TipDialog extends Dialog {

	public TipDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String msg;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder msg(String msg) {
			this.msg = msg;
			return this;
		}

		public TipDialog create() {

			final TipDialog dialog = new TipDialog(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_tip, null);
			// 加上这一句，取消原来的标题栏，没加这句之前，发现在三星的手机上会有一条蓝色的线
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			TextView msgTV = view.findViewById(R.id.tv_tip_message);
			msgTV.setText(this.msg);

			return dialog;
		}

	}
}
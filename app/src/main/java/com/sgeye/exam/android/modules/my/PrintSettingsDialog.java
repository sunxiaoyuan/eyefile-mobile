package com.sgeye.exam.android.modules.my;

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

import com.sgeye.exam.android.R;


public class PrintSettingsDialog extends Dialog {

	public PrintSettingsDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private OnChangePrintSettings mOnChangePrintSettings;
		private int mIndex;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder onChangePrintSettings(OnChangePrintSettings onChangePrintSettings) {
			this.mOnChangePrintSettings = onChangePrintSettings;
			return this;
		}

		public Builder distance(int distance) {
			this.mIndex = distance;
			return this;
		}

		public PrintSettingsDialog create() {

			final PrintSettingsDialog dialog = new PrintSettingsDialog(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_print_settings, null);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			final ImageView iv_不打印 = (ImageView) view.findViewById(R.id.iv_不打印);
			final ImageView iv_打印带回执 = (ImageView) view.findViewById(R.id.iv_打印带回执);
			final ImageView iv_打印不带回执 = (ImageView) view.findViewById(R.id.iv_打印不带回执);

			if (mIndex == 0) {
				iv_不打印.setImageResource(R.mipmap.select);
				iv_打印带回执.setImageResource(R.mipmap.unselect);
				iv_打印不带回执.setImageResource(R.mipmap.unselect);
			} else if (mIndex == 1) {
				iv_不打印.setImageResource(R.mipmap.unselect);
				iv_打印带回执.setImageResource(R.mipmap.select);
				iv_打印不带回执.setImageResource(R.mipmap.unselect);
			} else if (mIndex == 2) {
				iv_不打印.setImageResource(R.mipmap.unselect);
				iv_打印带回执.setImageResource(R.mipmap.unselect);
				iv_打印不带回执.setImageResource(R.mipmap.select);
			}

			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					switch (id) {
						case R.id.ll_不打印:
							iv_不打印.setImageResource(R.mipmap.select);
							iv_打印带回执.setImageResource(R.mipmap.unselect);
							iv_打印不带回执.setImageResource(R.mipmap.unselect);
							mOnChangePrintSettings.onChangePrintSettings(0);
							break;
						case R.id.ll_打印带回执:
							iv_不打印.setImageResource(R.mipmap.unselect);
							iv_打印带回执.setImageResource(R.mipmap.select);
							iv_打印不带回执.setImageResource(R.mipmap.unselect);
							mOnChangePrintSettings.onChangePrintSettings(1);
							break;
						case R.id.ll_打印不带回执:
							iv_不打印.setImageResource(R.mipmap.unselect);
							iv_打印带回执.setImageResource(R.mipmap.unselect);
							iv_打印不带回执.setImageResource(R.mipmap.select);
							mOnChangePrintSettings.onChangePrintSettings(2);
							break;
						default:
							break;
					}
					// 关闭对话框
					dialog.dismiss();
				}
			};

			RelativeLayout ll_不打印 = (RelativeLayout) view.findViewById(R.id.ll_不打印);
			ll_不打印.setOnClickListener(listener);
			RelativeLayout ll_打印带回执 = (RelativeLayout) view.findViewById(R.id.ll_打印带回执);
			ll_打印带回执.setOnClickListener(listener);
			RelativeLayout ll_打印不带回执 = (RelativeLayout) view.findViewById(R.id.ll_打印不带回执);
			ll_打印不带回执.setOnClickListener(listener);

			return dialog;
		}


	}
}
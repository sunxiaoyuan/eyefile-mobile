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

import com.sgeye.exam.android.R;


// 视频查看弹窗
public class DistanceDialog extends Dialog {

	public DistanceDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private OnChangeDistance mOnChangeDistanceListener;
		private int mDistance;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder onChangeDistance(OnChangeDistance onChangeDistanceListener) {
			this.mOnChangeDistanceListener = onChangeDistanceListener;
			return this;
		}

		public Builder distance(int distance) {
			this.mDistance = distance;
			return this;
		}

		public DistanceDialog create() {

			final DistanceDialog dialog = new DistanceDialog(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_distance, null);
			// 加上这一句，取消原来的标题栏，没加这句之前，发现在三星的手机上会有一条蓝色的线
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			final ImageView m2ImageView = (ImageView) view.findViewById(R.id.iv_distance_2m);
			final ImageView m5ImageView = (ImageView) view.findViewById(R.id.iv_distance_5m);

			if (mDistance == 2) {
				m2ImageView.setImageResource(R.mipmap.select);
				m5ImageView.setImageResource(R.mipmap.unselect);
			} else if (mDistance == 5){
				m2ImageView.setImageResource(R.mipmap.unselect);
				m5ImageView.setImageResource(R.mipmap.select);
			}

			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					switch (id) {
						case R.id.ll_distance_2m:
							m2ImageView.setImageResource(R.mipmap.select);
							m5ImageView.setImageResource(R.mipmap.unselect);
							mOnChangeDistanceListener.onChangeDistance(2);
							break;
						case R.id.ll_distance_5m:
							m2ImageView.setImageResource(R.mipmap.unselect);
							m5ImageView.setImageResource(R.mipmap.select);
							mOnChangeDistanceListener.onChangeDistance(5);
							break;
						default:
							break;
					}
					// 关闭对话框
					dialog.dismiss();
				}
			};

			RelativeLayout m2View = (RelativeLayout) view.findViewById(R.id.ll_distance_2m);
			m2View.setOnClickListener(listener);
			RelativeLayout m5View = (RelativeLayout) view.findViewById(R.id.ll_distance_5m);
			m5View.setOnClickListener(listener);

			return dialog;
		}

	}
}
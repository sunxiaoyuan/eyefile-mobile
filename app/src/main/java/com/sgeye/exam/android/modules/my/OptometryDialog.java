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

import java.util.ArrayList;
import java.util.List;


public class OptometryDialog extends Dialog {

	public OptometryDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private OnChangeOptometry mOnChangeOptometry;
		private int mIndex;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder onChangeOptometry(OnChangeOptometry onChangeOptometry) {
			this.mOnChangeOptometry = onChangeOptometry;
			return this;
		}

		public Builder distance(int distance) {
			this.mIndex = distance;
			return this;
		}

		public OptometryDialog create() {

			final OptometryDialog dialog = new OptometryDialog(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_optometry, null);
			// 加上这一句，取消原来的标题栏，没加这句之前，发现在三星的手机上会有一条蓝色的线
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			final ImageView iv_拓普康 = (ImageView) view.findViewById(R.id.iv_拓普康);
			final ImageView iv_索维 = (ImageView) view.findViewById(R.id.iv_索维);
			final ImageView iv_尼德克 = (ImageView) view.findViewById(R.id.iv_尼德克);
			final ImageView iv_天乐 = (ImageView) view.findViewById(R.id.iv_天乐);
			final ImageView iv_新缘 = (ImageView) view.findViewById(R.id.iv_新缘);
			final ImageView iv_法里奥 = (ImageView) view.findViewById(R.id.iv_法里奥);
			final ImageView iv_其他 = (ImageView) view.findViewById(R.id.iv_其他);

			List<ImageView> imageViews = new ArrayList<>();
			imageViews.add(iv_拓普康);
			imageViews.add(iv_索维);
			imageViews.add(iv_尼德克);
			imageViews.add(iv_天乐);
			imageViews.add(iv_新缘);
			imageViews.add(iv_法里奥);
			imageViews.add(iv_其他);

			for (int i = 0; i < imageViews.size() - 1; i++) {
				ImageView imageView = imageViews.get(i);
				if (i == mIndex){
					imageView.setImageResource(R.mipmap.select);
				} else {
					imageView.setImageResource(R.mipmap.unselect);
				}
			}

			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					int curIndex = 0;
					switch (id) {
						case R.id.ll_拓普康:
							curIndex = 0;
							break;
						case R.id.ll_索维:
							curIndex = 1;
							break;
						case R.id.ll_尼德克:
							curIndex = 2;
							break;
						case R.id.ll_天乐:
							curIndex = 3;
							break;
						case R.id.ll_新缘:
							curIndex = 4;
							break;
						case R.id.ll_法里奥:
							curIndex = 5;
							break;
						case R.id.ll_其他:
							curIndex = 6;
							break;
						default:
							break;
					}
					for (int i = 0; i < imageViews.size() - 1; i++) {
						ImageView imageView = imageViews.get(i);
						if (i == curIndex){
							imageView.setImageResource(R.mipmap.select);
						} else {
							imageView.setImageResource(R.mipmap.unselect);
						}
					}
					mOnChangeOptometry.onChangeOptometry(curIndex);
					// 关闭对话框
					dialog.dismiss();
				}
			};

			RelativeLayout ll_拓普康 = (RelativeLayout) view.findViewById(R.id.ll_拓普康);
			ll_拓普康.setOnClickListener(listener);
			RelativeLayout ll_索维 = (RelativeLayout) view.findViewById(R.id.ll_索维);
			ll_索维.setOnClickListener(listener);
			RelativeLayout ll_尼德克 = (RelativeLayout) view.findViewById(R.id.ll_尼德克);
			ll_尼德克.setOnClickListener(listener);
			RelativeLayout ll_天乐 = (RelativeLayout) view.findViewById(R.id.ll_天乐);
			ll_天乐.setOnClickListener(listener);

			RelativeLayout ll_新缘 = (RelativeLayout) view.findViewById(R.id.ll_新缘);
			ll_新缘.setOnClickListener(listener);
			RelativeLayout ll_法里奥 = (RelativeLayout) view.findViewById(R.id.ll_法里奥);
			ll_法里奥.setOnClickListener(listener);
			RelativeLayout ll_其他 = (RelativeLayout) view.findViewById(R.id.ll_其他);
			ll_其他.setOnClickListener(listener);

			return dialog;
		}


	}
}
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


public class RefractionCheckDialog extends Dialog {

    public RefractionCheckDialog(Context context) {
        super(context);
    }

    public static class Builder {


        private Context context;
        private OnChangeRefrection mOnChangeRefrectionListener;
        private int mIndex;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onChangeRefrection(OnChangeRefrection onChangeRefrection) {
            this.mOnChangeRefrectionListener = onChangeRefrection;
            return this;
        }

        public Builder distance(int distance) {
            this.mIndex = distance;
            return this;
        }

        public RefractionCheckDialog create() {

            final RefractionCheckDialog dialog = new RefractionCheckDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_refraction_check, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final ImageView iv_伟伦 = (ImageView) view.findViewById(R.id.iv_伟伦);
            final ImageView iv_索维 = (ImageView) view.findViewById(R.id.iv_索维);
            final ImageView iv_莫廷 = (ImageView) view.findViewById(R.id.iv_莫廷);
            final ImageView iv_其他 = (ImageView) view.findViewById(R.id.iv_其他);

            if (mIndex == 0) {
                iv_伟伦.setImageResource(R.mipmap.select);
                iv_索维.setImageResource(R.mipmap.unselect);
                iv_莫廷.setImageResource(R.mipmap.unselect);
                iv_其他.setImageResource(R.mipmap.unselect);
            } else if (mIndex == 1) {
                iv_伟伦.setImageResource(R.mipmap.unselect);
                iv_索维.setImageResource(R.mipmap.select);
                iv_莫廷.setImageResource(R.mipmap.unselect);
                iv_其他.setImageResource(R.mipmap.unselect);
            } else if (mIndex == 2) {
                iv_伟伦.setImageResource(R.mipmap.unselect);
                iv_索维.setImageResource(R.mipmap.unselect);
                iv_莫廷.setImageResource(R.mipmap.select);
                iv_其他.setImageResource(R.mipmap.unselect);
            } else if (mIndex == 3) {
                iv_伟伦.setImageResource(R.mipmap.unselect);
                iv_索维.setImageResource(R.mipmap.unselect);
                iv_莫廷.setImageResource(R.mipmap.unselect);
                iv_其他.setImageResource(R.mipmap.select);
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    switch (id) {
                        case R.id.ll_伟伦:
                            iv_伟伦.setImageResource(R.mipmap.select);
                            iv_索维.setImageResource(R.mipmap.unselect);
                            iv_莫廷.setImageResource(R.mipmap.unselect);
                            iv_其他.setImageResource(R.mipmap.unselect);
                            mOnChangeRefrectionListener.onChangeRefrection(0);
                            break;
                        case R.id.ll_索维:
                            iv_伟伦.setImageResource(R.mipmap.unselect);
                            iv_索维.setImageResource(R.mipmap.select);
                            iv_莫廷.setImageResource(R.mipmap.unselect);
                            iv_其他.setImageResource(R.mipmap.unselect);
                            mOnChangeRefrectionListener.onChangeRefrection(1);
                            break;
                        case R.id.ll_莫廷:
                            iv_伟伦.setImageResource(R.mipmap.unselect);
                            iv_索维.setImageResource(R.mipmap.unselect);
                            iv_莫廷.setImageResource(R.mipmap.select);
                            iv_其他.setImageResource(R.mipmap.unselect);
                            mOnChangeRefrectionListener.onChangeRefrection(2);
                            break;
                        case R.id.ll_其他:
                            iv_伟伦.setImageResource(R.mipmap.unselect);
                            iv_索维.setImageResource(R.mipmap.unselect);
                            iv_莫廷.setImageResource(R.mipmap.unselect);
                            iv_其他.setImageResource(R.mipmap.select);
                            mOnChangeRefrectionListener.onChangeRefrection(3);
                            break;
                        default:
                            break;
                    }
                    // 关闭对话框
                    dialog.dismiss();
                }
            };

            RelativeLayout ll_伟伦 = (RelativeLayout) view.findViewById(R.id.ll_伟伦);
            ll_伟伦.setOnClickListener(listener);
            RelativeLayout ll_索维 = (RelativeLayout) view.findViewById(R.id.ll_索维);
            ll_索维.setOnClickListener(listener);
            RelativeLayout ll_莫廷 = (RelativeLayout) view.findViewById(R.id.ll_莫廷);
            ll_莫廷.setOnClickListener(listener);
            RelativeLayout ll_其他 = (RelativeLayout) view.findViewById(R.id.ll_其他);
            ll_其他.setOnClickListener(listener);

            return dialog;
        }


    }
}
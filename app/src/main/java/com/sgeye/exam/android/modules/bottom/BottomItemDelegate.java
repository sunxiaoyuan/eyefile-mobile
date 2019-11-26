package com.sgeye.exam.android.modules.bottom;

import android.widget.Toast;

import com.sgeye.exam.android.R;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.delegates.MargaretDelegate;

import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by sunzhongyuan on 2018/10/13.
 * 菜单栏管理的delegate
 */

public abstract class BottomItemDelegate extends MargaretDelegate {

	// 再点一次退出程序时间设置
	private static final long WAIT_TIME = 2000L;
	private long TOUCH_TIME = 0;

	// 点击了返回按钮
	@Override
	public boolean onBackPressedSupport() {
		if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
			_mActivity.finish();
		} else {
			TOUCH_TIME = System.currentTimeMillis();
			Toast.makeText(_mActivity, "双击退出" + Margaret.getApplicationContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return super.onCreateFragmentAnimator();
	}
}

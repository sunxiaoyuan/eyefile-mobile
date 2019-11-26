package com.sgeye.exam.android.modules.bottom;

import android.graphics.Color;

import com.sgeye.exam.android.R;
import com.sgeye.exam.android.modules.clinic.ClinicHyberDelegate;
import com.sgeye.exam.android.modules.my.MyDelegate;
import com.sgeye.exam.android.modules.school.SchoolHyberDelegate;

import java.util.LinkedHashMap;

/**
 * Created by sunzhongyuan on 2018/10/13.
 * 底部栏delegate
 */

public class BottomDelegate extends BaseBottomDelegate {

	// 设置item
	@Override
	public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
		final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
		items.put(new BottomTabBean(R.mipmap.item00, R.mipmap.hitem00, "门诊筛查"), new ClinicHyberDelegate());
		items.put(new BottomTabBean(R.mipmap.item01, R.mipmap.hitem01, "学校筛查"), new SchoolHyberDelegate());
		items.put(new BottomTabBean(R.mipmap.item02, R.mipmap.hitem02, "我的"), new MyDelegate());
		return builder.addItems(items).build();
	}

	@Override
	public int setIndexDelegate() {
		return 0;
	}

	@Override
	public int setClickedColor() {
		return Color.parseColor("#00A97E");
	}

	@Override
	public int setUnClickedColor() {
		return Color.parseColor("#CDCFE1");
	}


}

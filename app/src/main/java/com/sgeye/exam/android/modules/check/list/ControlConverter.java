package com.sgeye.exam.android.modules.check.list;

import com.simon.margaret.ui.recycler.DataConverter;
import com.simon.margaret.ui.recycler.MultipleFields;
import com.simon.margaret.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by xll on 2018/11/9.
 */

// 关注日志
public class ControlConverter extends DataConverter {
	@Override
	public ArrayList<MultipleItemEntity> convert() {
		List<String> stringList = Arrays.asList("2.0", "1.5", "1.2", "1.0", "0.8",
				"0.6", "0.5", "0.4", "0.3", "0.25", "0.2", "0.15", "0.12", "0.1");
		Collections.reverse(stringList);
		final int size = stringList.size();
		for (int i = 0; i < size; i++) {
			final MultipleItemEntity entity = MultipleItemEntity.builder()
					.setField(ControlFields.NUMBER, stringList.get(i))
					.setField(MultipleFields.ITEM_TYPE, ControlItemType.NORMAL)
					.setField(ControlFields.SELECTION, ItemSectionType.UNSELECTED)
					.build();
			if (i == 0) {  // 默认选中2.0
				entity.setField(ControlFields.SELECTION, ItemSectionType.SELECTED);
			}
			ENTITIES.add(entity);
		}
		return ENTITIES;
	}

}

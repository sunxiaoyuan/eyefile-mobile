package com.sgeye.exam.android.modules.check.list;

import android.view.View;

import com.sgeye.exam.android.R;
import com.sgeye.exam.android.constants.AppConstants;
import com.simon.margaret.app.Margaret;
import com.simon.margaret.ui.recycler.MultipleItemEntity;
import com.simon.margaret.ui.recycler.MultipleRecyclerAdapter;
import com.simon.margaret.ui.recycler.MultipleViewHolder;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.zhangke.websocket.WebSocketHandler;

import java.util.List;


/**
 * Created by Administrator on 2018/11/7.
 * 关注日志
 */

public class ControlAdapter extends MultipleRecyclerAdapter {

	public ControlAdapter(List<MultipleItemEntity> data) {
		super(data);
		addItemType(ControlItemType.NORMAL, R.layout.item_control);
	}

	@Override
	protected MultipleViewHolder createBaseViewHolder(View view) {
		return MultipleViewHolder.create(view);
	}

	@Override
	protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
		super.convert(holder, entity);
		switch (holder.getItemViewType()) {
			case ControlItemType.NORMAL:
				holder.setText(R.id.tv_item_control, (String) entity.getField(ControlFields.NUMBER));
				ItemSectionType selectionType = entity.getField(ControlFields.SELECTION);
				if (selectionType == ItemSectionType.SELECTED){
					// 选中状态
					holder.setTextColor(R.id.tv_item_control, Margaret.getApplicationContext().getResources().getColor(R.color.white));
					holder.setBackgroundColor(R.id.ll_item_bg, Margaret.getApplicationContext().getResources().getColor(R.color.mainColor));
				} else if (selectionType == ItemSectionType.UNSELECTED) {
					// 默认状态
					holder.setTextColor(R.id.tv_item_control, Margaret.getApplicationContext().getResources().getColor(R.color.chooseGray));
					holder.setBackgroundColor(R.id.ll_item_bg, Margaret.getApplicationContext().getResources().getColor(R.color.white));
				}
				break;
			default:
				break;
		}
	}
}


package com.sgeye.exam.android.modules.check.list;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.sgeye.exam.android.constants.AppConstants;
import com.simon.margaret.ui.recycler.MultipleItemEntity;
import com.simon.margaret.util.callback.CallbackManager;
import com.simon.margaret.util.callback.CallbackType;
import com.simon.margaret.util.callback.IGlobalCallback;
import com.zhangke.websocket.WebSocketHandler;


/**
 * Created by apple on 2019/11/21.
 */

public class ControlClickListener extends SimpleClickListener {

	private int mCurrentIndex = 9;

	@Override
	public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		// 改变上一个选中的样式
		MultipleItemEntity entityBefore = (MultipleItemEntity) adapter.getItem(mCurrentIndex);
		entityBefore.setField(ControlFields.SELECTION, ItemSectionType.UNSELECTED);

		// 改变当前样式
		MultipleItemEntity entity = (MultipleItemEntity) adapter.getItem(position);
		entity.setField(ControlFields.SELECTION, ItemSectionType.SELECTED);

		// 触发更新
		adapter.notifyItemChanged(mCurrentIndex);
		adapter.notifyItemChanged(position);

		// 发送socket
		StringBuffer sendMsg = new StringBuffer();
		sendMsg.append(AppConstants.SOCKET_COMMAND_LINE_PRE).append((String) entity.getField(ControlFields.NUMBER));
		WebSocketHandler.getDefault().send(sendMsg.toString());

		mCurrentIndex = position;

		// 通知检查页面，当前行变更
		@SuppressWarnings("unchecked")
		final IGlobalCallback<Integer> callback = CallbackManager
				.getInstance()
				.getCallback(CallbackType.ON_CLICK);
		if (callback != null) {
			callback.executeCallback(mCurrentIndex);
		}
	}

	@Override
	public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

	}

	@Override
	public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

	}

	@Override
	public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

	}
}

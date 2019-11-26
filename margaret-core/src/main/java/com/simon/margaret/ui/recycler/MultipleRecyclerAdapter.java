package com.simon.margaret.ui.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by sunzhongyuan on 2018/10/16.
 */

public class MultipleRecyclerAdapter extends
        BaseMultiItemQuickAdapter<MultipleItemEntity, MultipleViewHolder>
        implements
        BaseQuickAdapter.SpanSizeLookup,
        OnItemClickListener {

    protected MultipleRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        init();
    }

    public static MultipleRecyclerAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecyclerAdapter(data);
    }

    public static MultipleRecyclerAdapter create(DataConverter converter) {
        return new MultipleRecyclerAdapter(converter.convert());
    }

    public void refresh(List<MultipleItemEntity> data) {
        getData().clear();
        setNewData(data);
        notifyDataSetChanged();
    }

    private void init() {
        // 设置不同的item布局
//        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
//        addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
//        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_image_text);
//        addItemType(ItemType.BANNER, R.layout.item_multiple_banner);
        // 设置宽度监听
        setSpanSizeLookup(this);
        // 打开加载动画
        openLoadAnimation();
        // 多次执行动画
        isFirstOnly(false);
    }

    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    // 将entity的数据填充到holder页面的组件中
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
//        final String text;
//        final String imageUrl;
//        final ArrayList<String> bannerImages;
//        switch (holder.getItemViewType()) {
//            case ItemType.TEXT:
//                text = entity.getField(MultipleFields.TEXT);
//                holder.setText(R.id.text_single, text);
//                break;
//            case ItemType.IMAGE:
//                imageUrl = entity.getField(MultipleFields.IMAGE_URL);
//                Glide.with(mContext)
//                        .load(imageUrl)
//                        .apply(RECYCLER_OPTIONS)
//                        .into((ImageView) holder.getView(R.id.img_single));
//                break;
//            case ItemType.TEXT_IMAGE:
//                text = entity.getField(MultipleFields.TEXT);
//                imageUrl = entity.getField(MultipleFields.IMAGE_URL);
//                Glide.with(mContext)
//                        .load(imageUrl)
//                        .apply(RECYCLER_OPTIONS)
//                        .into((ImageView) holder.getView(R.id.img_multiple));
//                holder.setText(R.id.tv_multiple, text);
//                break;
//            case ItemType.BANNER:
//                if (!mIsInitBanner) {
//                    bannerImages = entity.getField(MultipleFields.BANNERS);
//                    final ConvenientBanner<String> convenientBanner = holder.getView(R.id.banner_recycler_item);
//                    BannerCreator.setDefault(convenientBanner, bannerImages, this);
//                    mIsInitBanner = true;
//                }
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return getData().get(position).getField(MultipleFields.SPAN_SIZE);
    }

    @Override
    public void onItemClick(int position) {

    }
}

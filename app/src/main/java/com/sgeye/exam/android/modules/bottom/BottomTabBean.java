package com.sgeye.exam.android.modules.bottom;

/**
 * Created by sunzhongyuan on 2018/10/13.
 * 底边兰item，设置文字和图标
 */

public final class BottomTabBean {

    private final int IMAGE_ID;
    private final int CLICKED_IMAGE_ID;
    private final CharSequence TITLE;

    public BottomTabBean(int imageId, int clickedImageId, CharSequence title) {
        this.IMAGE_ID = imageId;
        this.CLICKED_IMAGE_ID = clickedImageId;
        this.TITLE = title;
    }

    public int getIMAGE_ID() {
        return IMAGE_ID;
    }

    public int getCLICKED_IMAGE_ID() {
        return CLICKED_IMAGE_ID;
    }

    public CharSequence getTitle() {
        return TITLE;
    }
}

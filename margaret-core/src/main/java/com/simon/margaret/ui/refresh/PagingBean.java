package com.simon.margaret.ui.refresh;

/**
 * Created by sunzhongyuan on 2018/10/16.
 */

public final class PagingBean {

    // 当前是第几页
    private int mPageIndex = 1;
    // 总数据条数
    private int mTotal = 0;
    // 一页显示几条数据
    private int mPageSize = 9;
    // 当前已经显示了几条数据
    private int mCurrentCount = 0;

    public int getPageIndex() {
        return mPageIndex;
    }

    public PagingBean setPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
        return this;
    }

    public int getTotal() {
        return mTotal;
    }

    public PagingBean setTotal(int mTotal) {
        this.mTotal = mTotal;
        return this;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public PagingBean setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
        return this;
    }

    public int getCurrentCount() {
        return mCurrentCount;
    }

    public PagingBean setCurrentCount(int mCurrentCount) {
        this.mCurrentCount = mCurrentCount;
        return this;
    }

    public PagingBean addIndex() {
        mPageIndex++;
        return this;
    }

    public void reset() {
        this.mPageSize = 9;
        this.mCurrentCount = 0;
        this.mTotal = 0;
        this.mPageIndex = 1;
    }
}

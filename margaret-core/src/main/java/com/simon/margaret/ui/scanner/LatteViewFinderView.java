package com.simon.margaret.ui.scanner;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.simon.margaret.R;
import com.simon.margaret.app.Margaret;

import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by 傅令杰
 */

public class LatteViewFinderView extends ViewFinderView {

    public LatteViewFinderView(Context context) {
        this(context, null);
    }

    public LatteViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;
        mBorderPaint.setColor(Margaret.getApplicationContext().getResources().getColor(R.color.mainGreen));
        mLaserPaint.setColor(Margaret.getApplicationContext().getResources().getColor(R.color.mainGreen));
    }
}

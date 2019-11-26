package com.simon.margaret.delegates;

import android.view.View;

/**
 * Created by apple on 2019/8/22.
 */

public abstract class MargaretDelegate extends PermissionCheckerDelegate {

    @SuppressWarnings("unchecked")
    public <T extends MargaretDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }

}

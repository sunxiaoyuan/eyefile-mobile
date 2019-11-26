package com.simon.margaret.app;

import com.simon.margaret.util.storage.MargaretPreference;

/**
 * Created by sunzhongyuan on 2018/10/10.
 */
// 用户登录状态业务类
public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登录状态，登录后调用
    public static void setSignState(boolean state) {
        MargaretPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    public static boolean isSignIn() {
        return MargaretPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    // 判断是否已经登录
    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}

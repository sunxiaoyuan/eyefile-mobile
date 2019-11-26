package com.simon.margaret.app;

/**
 * Created by sunzhongyuan on 2018/10/10.
 */
// 检查用户登录状态的接口
public interface IUserChecker {

    void onSignIn(); // 已登录

    void onNotSignIn(); // 未登录
}

package com.sgeye.exam.android.modules.check;

public class CheckResult {

    public String ucvaOd = ""; // 远-右-裸
    public String ucvaOs = ""; // 远-左-裸
    public String cvaOd = ""; // 远-右-镜
    public String cvaOs = ""; // 远-左-镜

    public CheckResult() {
    }

    public CheckResult(String ucvaOd, String ucvaOs, String cvaOd, String cvaOs) {
        this.ucvaOd = ucvaOd;
        this.ucvaOs = ucvaOs;
        this.cvaOd = cvaOd;
        this.cvaOs = cvaOs;
    }
}

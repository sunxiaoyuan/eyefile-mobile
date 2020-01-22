package com.sgeye.exam.android.event.bean;

/**
 * Created by apple on 2019/11/25.
 */

public class CheckPadEventBean extends PrintEventBean {

    public String studentId;
    public String etaskId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEtaskId() {
        return etaskId;
    }

    public void setEtaskId(String etaskId) {
        this.etaskId = etaskId;
    }
}

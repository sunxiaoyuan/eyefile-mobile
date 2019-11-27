package com.simon.margaret.util.callback;

/**
 * Created by 傅令杰
 */

public enum CallbackType {
    ON_CROP,
    TAG_OPEN_PUSH,
    TAG_STOP_PUSH,

    // 网页调用原生方法相关
    ON_JS_CALL_NATIVE_QR_CLINIC,
    ON_JS_CALL_NATIVE_QR_SCHOOL,
    ON_JS_CALL_NATIVE_LOGIN,
    ON_JS_CALL_NATIVE_TAKE_PHOTO_CLINIC,
    ON_JS_CALL_NATIVE_TAKE_PHOTO_SCHOOL,
    ON_JS_CALL_NATIVE_CHECK_PAD,

    // webview相关
    ON_WEBVIEW_READY_CLINIC,
    ON_WEBVIEW_READY_SCHOOL,
    ON_WEBVIEW_READY_SIGHT,

    ON_MESSAGE_CHANGE_LINE,
    ON_MESSAGE_RESULT,
    ON_CLICK,


    // 扫描二维码相关
    ON_CLINIC_SCAN,
    ON_SCHOOL_SCAN,
    ON_CONN_DEVICE_SCAN,
}

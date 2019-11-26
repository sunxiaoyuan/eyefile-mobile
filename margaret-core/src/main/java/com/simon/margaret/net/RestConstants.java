package com.simon.margaret.net;

/**
 * Created by apple on 2019/4/3.
 */

public class RestConstants {

    // 设备采集模块
    // 摄像机画面控制-缩放
    public static final String URL_CONTROL_VIDEO_CAMERA = "grasp/controlVideoCamera";
    // 摄像机云台复位
    public static final String URL_RESTORE_PLATFORM = "grasp/restorePlatform";
    // 任务启动
    public static final String URL_TASK_START = "task/start";
    // 任务结束
    public static final String URL_TASK_STOP = "task/stop";
    // 云台方向控制
    public static final String URL_CONTROL_PLATFORM = "grasp/controlPlatform";
    // 切换使用相机
    public static final String URL_SWITCH_CAMERA = "grasp/switchCamera";
    // 查询当前是否存在任务
    public static final String URL_TASK_STATUS = "task/state";
    // 拍照
    public static final String URL_TAKE_PIC = "image/photograph";

    // 图片管理模块
    // 图片查询
    public static final String URL_IMAGE_FIND = "image/find";
    // 普通图片标记病害
    public static final String URL_IMAGE_MARK = "image/mark";
    // 手动抽取
    public static final String URL_IMAGE_EXTRACT = "image/extract";
    // 关注类型获取
    public static final String URL_IMAGE_CONCERNTYPE = "image/concernType";
    // 查询图片信息
    public static final String URL_IMAGE_DETAIL = "image/detail";

    // 测绘统计模块
    // 车辆轨迹管理
    public static final String URL_VEHICLETASK_GPS = "vehicleTask/gps/";

    // 系统模块
    // 修改、获取系统参数
    public static final String URL_SYSTEM_PHOTOGRAPH_PARAM = "system/photographParam";
    // 修改登录密码
    public static final String URL_CHANGE_PSW = "login/password";
    // 关闭巡查宝
    public static final String URL_SHUT_DOWN = "task/shutdown";

    // 日志模块
    // 设备日志查询接口
    public static final String URL_LOG_DEVICESERCH = "log/getEquipLog";
    // 操作日志查询接口
    public static final String URL_LOG_OPERATERSERCH = "log/getOperationLog";

    // 数据同步模块
    // 启动同步日志接口
    public static final String URL_SYNC_LOG = "sync/log";
    // 启动同步图片接口
    public static final String URL_SYNC_IMAGE = "sync/image";
    // 启动同步轨迹及位置接口
    public static final String URL_SYNC_COORDINATES = "sync/coordinates";
    // 检查各个同步的进度
    public static final String URL_SYNC_CHECK = "sync/getSyncStatus";

    // 登录模块
    // 登录接口
    public static final String URL_LOGIN = "login/loginSystem";
    // 同步账户信息接口
    public static final String URL_LOGIN_SYNCINFO = "login/syncinfo";

    // 操作日志记录接口
    public static final String URL_OP_LOG_WRITE = "task/add/operationLog";

    // -------------------------  远程服务器URL --------------------- //

    // 系统帮助
    public static final String SYS_HELP_URL = "http://114.250.29.58:81/systemhelp09.html";
    // 修改密码
    public static final String EDIT_PSW = "http://114.250.29.58:8089/longen_service/login/password";
    // 登录
    public static final String GET_AUTH = "http://114.250.29.58:8089/longen_service/login/login/pad";
    // 获取用户信息
    public static final String GET_USER_INFO = "http://114.250.29.58:8089/longen_service/login/";
    // 下载apk
    public static final String DOWNLOAD_APK_URL = "http://114.250.29.58:81/apk/pad/longenpad.apk";
    // 查询最新版本号
    public static final String CHECK_LATEST_VERSION = "http://114.250.29.58:81/apk/pad/version.json";



}

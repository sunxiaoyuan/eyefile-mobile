package com.simon.margaret.net;

/**
 * Created by sunzhongyuan on 2018/12/10.
 */

public enum RestCode {

    // -------------------------------------- 云服务器 ---------------------------- //
    //系统级别
    REQUEST_METHOD_ERROR_1(-4, "请求方式错误"),
    STATUS_SYSTEM_LOG_ERROR_1(-3, "后台日志错误"),
    STATUS_SHUJU_ERR_1(-2, "第三方接口问题"),
    STATUS_SYSTEM_ERROR_1(-1, " 系统错误"),
    STATUS_SUCCESS_1(0, "成功"),
    STATUS_NAME_REPETITION_1(1, "数据重复"),
    STATUS_SEARCH_DATA_NULL_1(2, "查询数据为空"),
    STATUS_INCOMING_DATA_NULL_1(3, "非空验证不通过"),
    VALIDATION_IS_NOT_PASSED_1(4, "参数验证不通过"),
    STATUS_CHILD_RELEVANCE_1(5, "获取用户信息失败"),
    STATUS_CHILD_REPETITION_1(6, "未查找到用户信息"),
    EXISTS_CHILD_RELEVANCE_1(7, "密码错误"),
    NOT_HAVE_THIS_PERMISSION_1(8, "未经授权,您无权使用该权限"),
    NEED_ADMIN_STATUS_1(9, "需要管理员角色,进行该操作"),
    NOT_DELETED_ADMIN_1(10, "该用户是管理员角色，无法删除、禁用或修改"),
    STATUS_USEING_1(11, "数据正在使用,无法删除或禁用"),
    THE_DATA_DISABLE_STATE_1(12, "该数据为禁用状态"),
    TWO_PASSWORD_INCONSISTENCY_1(13, "两次密码输入不一致"),
    NEWOLDPASSWORD_REPEAT_1(14, "新旧密码重复"),
    OLD_PASSWORD_INCORRECT_1(15, "旧密码输入错误"),
    DATA_STATUS_ERR_1(16, "数据已经删除，无法进行操作"),
    NON_TREE_STRUCTURE_1(17, "非树状结构"),
    TYPE_MISMATCH_1(18,"不允许上传该文件类型"),
    IMPORT_FAILED_1(19,"读取Excel文件失败,请重新检查"),
    DATA_RULE_NOT_CORRECT_1(20,"导入Excel数据的表头异常,需重新检查"),
    DATA_ILLEGALITY_1(21,"数据库数据非法"),
    NOT_LEVELING_1(22,"不能创建同级"),
    DETP_READ_ONLY_1(23, "单位管理只能分配只读权限"),
    FILE_DOES_NOT_EXIST_1(24, "文件不存在"),
    //项目级别
    STATUS_EQUIPMENT_TYPE_ERR_1(150, "设备类型不能修改"),
    STATUS_UPDATASTATUS_ERR_1(151, "状态不统一（不是启用/停用）"),
    EQUIPMENT_TYPE_ERR_1(152, "数据类型不存在"),
    PARENTID_ERR_1(153, "ParentId不合法"),
    PARENTID_ERR_STOP_1(154, "ParentId被禁用，当前数据无法启用，需要先启用ParentId的数据"),
    NOT_AI_1(155, "不是ai设备，不能设置启用停用ai状态"),
    //用户操作
    USERNAME_EXISTS_1(262, "用户名已存在"),
    EMAIL_EXISTS_1(263, "邮箱已存在"),
    TELEPHONE_EXISTS_1(264, "手机号已存在"),
    //用户登录状态
    ACCOUNT_OR_PASSWORD_ERROR_1(304, "账号或密码错误"),
    NEED_TO_LOGIN_1(305, "需要用户登陆"),
    NEED_TO_AUTHORITY_1(306, "需要权限"),
    ACCOUNT_LOCKING_1(307, "账号已被锁定,请联系管理员"),
    NO_APP_LOGIN_ACL_1(308, "无app端登陆权限"),
    NO_PAD_LOGIN_ACL_1(309, "无pad端登陆权限"),
    NO_WEB_LOGIN_ACL_1(310, "无web端登陆权限"),

    // ------------------------------ 巡查宝 ------------------------------ //

    // 负数 异常
    REQUEST_METHOD_ERROR(-4, "请求方式错误"),
    STATUS_SYSTEM_LOG_ERROR(-3, "后台日志错误"),
    STATUS_SHUJU_ERR(-2, "第三方接口问题"),
    STATUS_SYSTEM_ERROR(-1, " 系统错误"),
    STATUS_SUCCESS(0, "成功"),
    // 1000 系统级别
    STATUS_NAME_REPETITION(1001, "数据重复"),
    STATUS_SEARCH_DATA_NULL(1002, "查询数据为空"),
    STATUS_INCOMING_DATA_NULL(1003, "非空验证不通过"),
    VALIDATION_IS_NOT_PASSED(1004, "参数验证不通过"),
    STATUS_CHILD_RELEVANCE(1005, "存在子节点,不能删除或禁用此数据"),
    EXISTS_CHILD_RELEVANCE(1006, "存在子节点,不能修改此数据节点位置"),
    STATUS_CHILD_REPETITION(1007, "子数据有重复信息"),
    STATUS_USEING(1008, "数据正在使用,无法删除或禁用"),
    DATA_DISABLE_STATE(1009, "该数据为禁用状态"),
    DATA_DISABLE_STOP(1010, "该数据为启用状态"),
    DATA_STATUS_ERR(1011, "数据已经删除或不存在,无法继续执行该动作"),
    NON_TREE_STRUCTURE(1012, "需要树状结构"),
    TYPE_MISMATCH(1013, "不允许上传该文件类型"),
    IMPORT_FAILED(1014, "读取Excel文件失败,请重新检查"),
    DATA_RULE_NOT_CORRECT(1015, "导入Excel数据的表头异常"),
    DATA_ILLEGALITY(1016, "数据库数据非法"),
    TIME_REPETITION(1017, "参数时间重叠"),
    PARAMETER_REPETITION(1018, "参数重复"),
    TIME_PARAMETER_ERROR(1019, "时间参数错误"),
    ALREADY_USED(1020, "该数据已经存在使用,请确认无任何位置使用该数据,再进行更改"),
    FILE_NOT_EXIST(1031, "文件不存在"),
    STATUS_EQUIPMENT_TYPE_ERR(1050, "设备类型不能修改"),
    STATUS_UPDATASTATUS_ERR(1051, "选中的被操作对象状态不具备一致性"),
    EQUIPMENT_TYPE_ERR(1052, "数据类型不存在"),
    PARENTID_ERR(1053, "父节点不合法"),
    PARENTID_ERR_STOP(1054, "父节点被禁用,当前数据无法启用,需先启用父节点"),
    POINT_NOT_ENOUGH(1058, "请标记4个点"),
    // 2000 用户操作
    USERNAME_EXISTS(2001, "用户名已存在"),
    EMAIL_EXISTS(2002, "邮箱已存在"),
    TELEPHONE_EXISTS(2003, "手机号已存在"),
    ACCOUNT_OR_PASSWORD_ERROR(2004, "账号或密码错误"),
    TWO_PASSWORD_INCONSISTENCY(2005, "两次密码输入不一致"),
    NEWOLDPASSWORD_REPEAT(2016, "新旧密码重复"),
    EXISTING_ONGOING_TASKS(2017, "存在正在进行的任务"),
    NO_EXISTING_ONGOING_TASKS(2018, "不存在正在进行的任务"),

    // 3000 权限相关
    NEED_TO_AUTHORITY(3001, "权限不足"),
    NEED_TO_LOGIN(3002, "需要用户登陆"),
    ACCOUNT_LOCKING(3003, "账号已被锁定"),
    NEED_ADMIN_STATUS(3004, "需要管理员角色,进行该操作"),
    NOT_HAVE_THIS_PERMISSION(3005, "未经授权,您无权使用该权限"),
    NOT_DELETED_ADMIN(3006, "该用户是管理员角色，无法删除、禁用或修改"),
    //云台控制
    YT_STATUS_CONTROL_ERR(40001,"云台控制失败"),
    YT_STATUS_LINK_ERR(40002,"云台链接失败"),
    YT_STATUS_CONTRO_UNREASONABLE(40003,"不在操作范围"),
    YT_STATUS_INIT_ERR(40004,"云台初始化失败"),
    //摄像头相关
    CAMERA_LINK_ERR(41001,"摄像头链接失败"),
    //惯导相关
    INS_LINK_ERR(42001,"惯导5分钟无数据"),
    //段博士相关
    DBS_LINK_ERR(43001,"AI模块连接失败"),

    // 同步相关
    ZIP_ERR(2019, "压缩失败"),
    BIT_FAULT(2021, "自检失败"),
    SYNCING(2022, "同步中"),
    NO_LOCATION(2023, "还没有接受经纬度信息");


    private int code;
    private String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getCodeMsg(int code) { //使用int类型
        for(RestCode sourceEnum:  RestCode.values()) {
            if(sourceEnum.getCode() == code) return sourceEnum.getMsg();
        }
        return "未知错误，请联系管理员";
    }
}

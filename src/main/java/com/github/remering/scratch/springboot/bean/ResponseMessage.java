package com.github.remering.scratch.springboot.bean;

public enum ResponseMessage {

    ;

    public static final String OK = "操作成功";
    public static final String NO_ACTION = "无需操作";
    public static final String VERIFICATION_CODE_SENT = "验证码已发送";


    public static final String INTERNAL_SERVER_ERROR = "服务器内部错误";
    public static final String EMAIL_ALREADY_IN_USE = "邮箱已被使用";
    public static final String WRONG_PASSWORD_CONFIRM = "重复密码和密码不一致";
    public static final String WRONG_VERIFICATION_CODE = "验证码不正确";
    public static final String WRONG_EMAIL_OR_PASSWORD = "邮箱或密码不正确";
    public static final String RETRY_LATER = "操作未成功，请重试";
    public static final String VERIFICATION_CODE_REQUEST_TOO_FREQUENTLY = "验证码请求过于频繁，请稍后再试";
    public static final String INSUFFICIENT_PERMISSION = "权限不足";
    public static final String NO_SUCH_QUESTION = "问题未找到";
}

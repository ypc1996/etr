package com.etr.em;

/**
 * Created by LJW on 2019/5/22 - 23:12
 */
public enum GlobalEnum {
    SUCESS(0,"操作成功"),
    ERROR(1,"操作失败"),
    NO_TOKEN_ERROR(10099,"无token,请重新登录."),
    NO_USER_ERROR(10100,"用户不存在,请重新登录."),
    VERIFY_FAILURE(10101,"token验证失败,请重新登录."),
    TIME_EXPIRED_ERROR(10102,"token过期,请重新登录.");


    private Integer errorCode;
    private String errorMsg;


    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    GlobalEnum(Integer statusCode, String msg) {
        this.errorCode =statusCode;
        this.errorMsg =msg;

    }

}

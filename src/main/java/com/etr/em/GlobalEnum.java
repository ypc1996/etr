package com.etr.em;

/**
 * Created by LJW on 2019/5/22 - 23:12
 */
public enum GlobalEnum {
    SUCESS(0,"操作成功"),
    ERROR(1,"操作失败");


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

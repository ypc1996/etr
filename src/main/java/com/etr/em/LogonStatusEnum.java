package com.etr.em;

/**
 * Created by LJW on 2019/5/21 - 23:57
 */
public enum LogonStatusEnum {
    SUCESS(0,"登录状态正常"),
    ERROR(1,"登录状态超时");


    private Integer errorCode;
    private String errorMsg;

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    LogonStatusEnum(Integer statusCode, String msg) {
        this.errorCode =statusCode;
        this.errorMsg =msg;

    }
    public static LogonStatusEnum getLogonStatusEnumByCode(Integer code){
        for(LogonStatusEnum logonStatusEnum: LogonStatusEnum.values() ){
            if(logonStatusEnum.getErrorCode()==code){
                return logonStatusEnum;
            }
        }
        return null;
    }
}

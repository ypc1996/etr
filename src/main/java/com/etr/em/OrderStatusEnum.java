package com.etr.em;

/**
 * Created by LJW on 2019/5/22 - 0:03
 */
public enum OrderStatusEnum {
    SUCESS(0,"订单完成"),
    CANCELING_ORDER(1,"退订中"),
    CANCELING_SUCCESS(2,"退订成功"),
    INPAYMENT(3,"代付款"),
    PAID(4,"已付款");

    private Integer errorCode;
    private String errorMsg;

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    OrderStatusEnum(Integer statusCode, String msg) {
        this.errorCode =statusCode;
        this.errorMsg =msg;

    }
    public static OrderStatusEnum getorderStatusEnumByCode(Integer code){
        for(OrderStatusEnum orderStatusEnum: OrderStatusEnum.values() ){
            if(orderStatusEnum.getErrorCode()==code){
                return orderStatusEnum;
            }
        }
        return null;
    }
}

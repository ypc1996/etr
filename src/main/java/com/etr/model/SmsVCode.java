package com.etr.model;

import java.util.Date;

/**
 * @Author: ypc
 * @Date: 2019/5/25 10:49
 * @Description:
 */
public class SmsVCode {
    private Integer id;
    private String mobile;
    private String vCode;
    private Date createTime;
    private String reStr;
    private Integer used;
    public SmsVCode(){}
    public SmsVCode(String mobile, String vCode, String reStr, Integer used) {
        this.mobile = mobile;
        this.vCode = vCode;
        this.reStr = reStr;
        this.used = used;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReStr() {
        return reStr;
    }

    public void setReStr(String reStr) {
        this.reStr = reStr;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}

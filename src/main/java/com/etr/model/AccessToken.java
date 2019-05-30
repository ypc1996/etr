package com.etr.model;

import java.util.Date;

/**
 * Created by LJW on 2019/5/25 - 21:37
 */
public class AccessToken {
    private String accessToken;
    private String openId;
    private Date createDate;
    private Date updateDate;
    private Integer id;
    public AccessToken(){}
    public AccessToken(String accessToken,String openId){
        this.accessToken = accessToken;
        this.openId = openId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getOpenid() {
        return openId;
    }

    public void setOpenid(String openid) {
        this.openId = openid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;

    }
}

package com.etr.controller;

import com.etr.util.WxPayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ypc on 2019-5-20.
 */
@RequestMapping("/login")
@RestController
public class LoginController {
    @Value("${appid}")
    private String appid;
    @Value("${secret}")
    private String secret;
    @Value("${grant_type}")
    private String grant_type;
    //微信小程序登录
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Map<String,Object> wxLogin(String code){
        String js_code=code;//登录获取的code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
        String responseStr= WxPayUtils.httpRequest(url,"GET",null);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=null;
        try {
            map = mapper.readValue(responseStr,Map.class);//readValue到一个原始数据类型.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * add by LJW on 2019-5-21
     * 获取小程序全局唯一后台接口调用凭据（access_token）。调调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存。
     * @param grantType
     * @param appid
     * @param secret
     * @return {"access_token": "ACCESS_TOKEN", "expires_in": 7200}
     *
     *
     * access_token	string	获取到的凭证
     * expires_in	number	凭证有效时间，单位：秒。目前是7200秒之内的值。
     * errcode	number	错误码
     * errmsg	string	错误信息
     */
    @RequestMapping("/wxAccessToken")
    public Map<String,String> getAccessToken(String grantType,String appid,String secret){

        String url = "GET https://api.weixin.qq.com/cgi-bin/token?grant_type="+grantType+"&appid="+appid+"&secret="+secret;
        String responseStr= WxPayUtils.httpRequest(url,"GET",null);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map=null;
        try {
            map = mapper.readValue(responseStr,Map.class);//readValue到一个原始数据类型.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}

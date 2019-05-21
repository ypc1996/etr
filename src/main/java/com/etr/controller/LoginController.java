package com.etr.controller;

import com.etr.util.WxPayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ypc on 2019-5-20.
 */
@RequestMapping("login")
@RestController
public class LoginController {
    //微信小程序登录
    @RequestMapping("/wxLogin")
    public Map<String,String> wxLogin(String code){
        String appid="";//小程序appid
        String secret="";//小程序appSecret
        String js_code=code;//登录获取的code
        String grant_type="";//授权类型，此处只需填写 authorization_code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
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

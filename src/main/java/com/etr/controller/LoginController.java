package com.etr.controller;

import com.etr.common.JsonResult;
import com.etr.em.GlobalEnum;
import com.etr.model.User;
import com.etr.service.UserService;
import com.etr.util.JWTUtils;
import com.etr.util.JsonResultUtil;
import com.etr.util.WxPayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private UserService userService;


    //微信小程序登录
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public JsonResult wxLogin(String code){
        String js_code=code;//登录获取的code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
        String responseStr= WxPayUtils.httpRequest(url,"GET",null);
        ObjectMapper mapper = new ObjectMapper();

        Map<String,Object> map=null;
        String token = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());
        try {

            map = mapper.readValue(responseStr,Map.class);//readValue到一个原始数据类型.
            String openid = (String)map.get("openid");
            //身份验证是否成功
            User user = userService.getUserInfobyOpenID((String)map.get(openid));
            if(user == null) {
                user.setOpenId(openid);
                user.setCreateTime(createTime);
                userService.addUser(user);
            }

            //返回token
            token = JWTUtils.getToken(user);
            if (token != null) {
                userService.addUser(user);
                //添加到token表
                userService.addToken(token,openid,createTime);
                return JsonResultUtil.createSucess(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResultUtil.createError(GlobalEnum.ERROR);
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

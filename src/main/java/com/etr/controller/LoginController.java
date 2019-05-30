package com.etr.controller;

import com.aliyuncs.exceptions.ClientException;
import com.etr.common.JsonResult;
import com.etr.em.GlobalEnum;
import com.etr.model.SmsVCode;
import com.etr.model.User;
import com.etr.service.SmsVcodeService;
import com.etr.service.UserService;
import com.etr.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
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
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    @Value("${signName}")
    private String signName;
    @Value("${templateCode}")
    private String templateCode;
    @Autowired
    private UserService userService;
    @Autowired
    private SmsVcodeService smsVcodeService;
    /**
     * add by ypc on 2019-5-21 微信小程序登录
     */
    //微信小程序登录
    @RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
    public JsonResult wxLogin(String code){
        String js_code=code;//登录获取的code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
        String responseStr= WxPayUtils.httpRequest(url,"GET",null);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=null;
        try {
            map = mapper.readValue(responseStr,Map.class);//readValue到一个原始数据类型.
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResultUtil.createError(GlobalEnum.ERROR);
        }
        String openId= (String) map.get("openid");
        User user=userService.findUserByOpenId(openId);
        if(ParamCheckUtil.checkParamsNotNull(user)){
            user.setLastLoginTime(new Date());
            if(userService.updateUser(user)>0)
                map.put("userId",user.getId());
        }else{
            User newUser=new User(openId,"123456",1,openId);
            if(userService.addUser(newUser)>0)
                map.put("userId",newUser.getId());
        }
        return JsonResultUtil.createSucess(map);
    }
    /**
     * add by ypc on 2019-5-21 发送验证码
     */
    @RequestMapping(value = "/sendSms", method = RequestMethod.GET)
    public JsonResult sendSms(String mobile){
        if(smsVcodeService.findByMobileVCode(mobile,null,0)!=null){
            JsonResultUtil.createError(GlobalEnum.SMS_REPEAT_ERROR);
        }
        int vCode=RandomUtils.getRamdomCode();
        String templateParam="{\"code\":\""+vCode+"\"}";
//        System.out.println(templateParam);
        JsonResult jsonResult= null;
        try {
            jsonResult = SendSmsUtils.sendSms(accessKeyId,accessKeySecret,mobile,"联途广告",templateCode,templateParam,"");
        } catch (ClientException e) {
            e.printStackTrace();
            return JsonResultUtil.createError(GlobalEnum.SMS_ERROR);
        }
        //调用成功写入数据库 sms_vcode
        ObjectMapper mapper = new ObjectMapper();
        String reStr="";
        try {
            reStr=mapper.writeValueAsString(jsonResult.getData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Integer result=smsVcodeService.addSmsVCode(new SmsVCode(mobile,String.valueOf(vCode),reStr,0));
        if(result>0)
            return jsonResult;
        return JsonResultUtil.createError(GlobalEnum.BIND_MOBILE_ERROR);
    }
    /**
     * add by ypc on 2019-5-21 绑定手机
     */
    @RequestMapping(value = "/bindMobile", method = RequestMethod.GET)
    public JsonResult bindMobile(String mobile,String vCode,String openId){
        SmsVCode smsVCode =smsVcodeService.findByMobileVCode(mobile,vCode,0);
        if(ParamCheckUtil.checkParamsNotNull(smsVCode)){
            User user=userService.findUserByOpenId(openId);
            user.setMobile(mobile);
            if(userService.updateUser(user)>0){
                smsVCode.setUsed(1);
                if(smsVcodeService.updateSmsVCode(smsVCode)>0)
                    return JsonResultUtil.createSucess(user);
            }
        }
        return JsonResultUtil.createError(GlobalEnum.BIND_MOBILE_ERROR);

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
    @RequestMapping("/test")
    public JsonResult test() {
        SmsVCode smsVCode=smsVcodeService.findByMobileVCode("test","test",0);
        smsVCode.setUsed(1);
        if(smsVcodeService.updateSmsVCode(smsVCode)>0)
            return JsonResultUtil.createSucess(smsVCode);
        return JsonResultUtil.createError(GlobalEnum.ERROR);
    }
}

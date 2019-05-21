package com.etr.controller;


import com.etr.util.WxPayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ypc on 2019-5-20.
 */
@RequestMapping("")
@RestController
public class PayController {
    @Value("${appid}")
    private String appid;
    @Value("${mch_id}")
    private String mch_id;
    @Value("${key}")
    private String key;
    @Value("${notify_url}")
    private String notify_url;
    @Value("${sign_type}")
    private String sign_type;
    @Value("${trade_type}")
    private String trade_type;
    @Value("${pay_url}")
    private String pay_url;
    //wx小程序支付统一下单
    //先登录
    @RequestMapping(value = "/wxPay", method = RequestMethod.POST)
    public Map<String,Object> wxLogin(String code,String orderCode,HttpServletRequest request){
        String appid="";//小程序appid
        String secret="";//小程序appSecret
        String js_code=code;//登录获取的code
        String grant_type="";//授权类型，此处只需填写 authorization_code
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
        String responseStr= WxPayUtils.httpRequest(url,"GET",null);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> resultMap=null;
        try {
            resultMap = mapper.readValue(responseStr,Map.class);//readValue到一个原始数据类型.
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取登陆的openid
        String openId=resultMap.get("openid");
        //生成的随机字符串
        String nonce_str = WxPayUtils.getRandomStringByLength(32);
        //商品名称
        String body="测试商品";
        //获取客户端的ip地址
        String spbill_create_ip = getIpAddr(request);
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str",nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no",orderCode);//商户订单号,自己的订单ID
        packageParams.put("total_fee", 100+"");//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);//支付成功后的回调地址
        packageParams.put("trade_type", trade_type);//支付方式
        packageParams.put("openid", openId );//用户的openID，自己获取
        String prestr = WxPayUtils.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = WxPayUtils.createSign(prestr, key, "utf-8").toUpperCase();
        packageParams.put("sign",mysign);
        packageParams.put("notify_url",notify_url);
        String xml=WxPayUtils.mapToXml(packageParams);
        //调用统一下单接口，并接受返回的结果
        String result = WxPayUtils.httpRequest(pay_url, "POST", xml);
        // 将解析结果存储在HashMap中
        Map map = WxPayUtils.xmlToMap(result);
        String return_code = (String) map.get("return_code");//返回状态码
        String result_code = (String) map.get("result_code");//返回状态码
        Map<String, Object> responseMap = new HashMap<String, Object>();//返回给小程序端需要的参数
        if (return_code == "SUCCESS" && return_code.equals(result_code)) {
            String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
            responseMap.put("nonceStr", nonce_str);
            responseMap.put("package", "prepay_id=" + prepay_id);
            responseMap.put("timeStamp", System.currentTimeMillis());//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //拼接签名需要的参数
            String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + System.currentTimeMillis();
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = WxPayUtils.createSign(stringSignTemp,key, "utf-8").toUpperCase();

            responseMap.put("paySign", paySign);
        }
        responseMap.put("appid",appid);
        return responseMap;
    }


    //这里是支付回调接口，微信支付成功后会自动调用
    @RequestMapping(value = "/wxNotify", method = RequestMethod.POST)
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";

        Map map = WxPayUtils.xmlToMap(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> validParams = WxPayUtils.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String prestr = WxPayUtils.createLinkString(validParams);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if (WxPayUtils.verify(prestr, (String) map.get("sign"), key, "utf-8")) {
                /**此处添加自己的业务逻辑代码start**/

                //注意要判断微信支付重复回调，支付成功后微信会重复的进行回调

                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
    //获取IP
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
    public boolean isNotEmpty(String str){
        if(str==null||"".equals(str)){
            return false;
        }
        return true;
    }
}

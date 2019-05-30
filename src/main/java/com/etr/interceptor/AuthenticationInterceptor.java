package com.etr.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.etr.common.JsonResult;
import com.etr.em.GlobalEnum;
import com.etr.model.AccessToken;
import com.etr.model.User;

import com.etr.service.AccessTokenService;
import com.etr.service.UserService;
import com.etr.util.JWTUtils;
import com.etr.util.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author: ypc
 * @Date: 2019/5/22 21:34
 * @Description:
 */
public class AuthenticationInterceptor implements HandlerInterceptor {


    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Value("${EXPIRE_TIME}")
    private long EXPIRE_TIME;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token

        //请求是否携带token
        if (token == null) {
            JsonResult jsonResult = JsonResultUtil.createError(GlobalEnum.NO_TOKEN_ERROR);
            responseMessage(response,response.getWriter(),jsonResult);
            return false;
//            throw new RuntimeException("无token，请重新登录");
        }

        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }

       User user = userService.findUserById(Integer.valueOf(userId));

        if (user == null) {
            JsonResult jsonResult = JsonResultUtil.createError(GlobalEnum.NO_USER_ERROR);
            responseMessage(response,response.getWriter(),jsonResult);
            return false;
//            throw new RuntimeException("用户不存在，请重新登录");
        }
        String openid = user.getOpenId();
        AccessToken accessToken =  accessTokenService.getAccessTokenbyOpenId(openid);
        Date createDate = accessToken.getCreateDate();
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getOpenId())).build();
        try {
           jwtVerifier. verify(token);
        } catch (JWTVerificationException e) {
            JsonResult jsonResult = JsonResultUtil.createError(GlobalEnum.VERIFY_FAILURE);
            responseMessage(response,response.getWriter(),jsonResult);
            return false;
//          throw new RuntimeException("401");
        }
        //token是否过期
        if(!JWTUtils.isExpires(createDate,EXPIRE_TIME)){
            JsonResult jsonResult = JsonResultUtil.createError(GlobalEnum.TIME_EXPIRED_ERROR);
            responseMessage(response,response.getWriter(),jsonResult);
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }


    /**
     * 返回信息给客户端
     *
     * @param response
     * @param out
     * @param jsonResult
     */
    private void responseMessage(HttpServletResponse response, PrintWriter out, JsonResult jsonResult) {
        response.setContentType("application/json; charset=utf-8");
        out.print(JSONObject.toJSONString(jsonResult));
        out.flush();
        out.close();
    }
}

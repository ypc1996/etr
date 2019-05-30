package com.etr.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.etr.model.User;

/**
 * @Author: ypc
 * @Date: 2019/5/22 21:32
 * @Description:
 */
public class JWTUtils {
    public static String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(String.valueOf(user.getId()))
                .sign(Algorithm.HMAC256(user.getOpenId()));
        return token;
    }
}

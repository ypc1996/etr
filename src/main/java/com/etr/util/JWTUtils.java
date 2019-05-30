package com.etr.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.etr.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ypc
 * @Date: 2019/5/22 21:32
 * @Description:
 */
public class JWTUtils {

    /**
     * 过期时间一天，
     * TODO 正式运行时修改为15分钟
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    private static String openid;
    /**
     * 生成签名,15min后过期
     *
     * @param user
     * @return 加密的token
     */
    public static String getToken(User user) {
        openid = user.getOpenId();
        //过期时间
//        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token="";
        token= JWT.create().withAudience(String.valueOf(user.getId()))
//                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(user.getOpenId()));

        return token;
    }



    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            JWT.decode(token).getAudience().get(0);
            Algorithm algorithm = Algorithm.HMAC256(openid);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("loginName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取登陆用户ID
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

//    /**
//     * 生成签名,15min后过期
//     *
//     * @param user
//     * @return 加密的token
//     */
//    public static String sign(String username,String userId) {
//    public static String sign(User user) {
////            过期时间
//            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
////            私钥及加密算法
//            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
////            设置头部信息
//            Map<String, Object> header = new HashMap<>(2);
//            header.put("typ", "JWT");
//            header.put("alg", "HS256");
//            // 附带username，userId信息，生成签名
//            return JWT.create()
//                    .withHeader(header)
//                    .withClaim("loginName", user.getId())
////                    .withClaim("userId",userId)
//                    .withExpiresAt(date)
//                    .sign(algorithm);
//    }

    public static boolean isExpires(Date nowtime, long timelimit) throws ParseException {


        long cuttentTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        long time = simpleDateFormat.parse(nowtime.toString()).getTime();
        if((cuttentTime-time) < timelimit){
            return true;
        }
        return false;
    }


}

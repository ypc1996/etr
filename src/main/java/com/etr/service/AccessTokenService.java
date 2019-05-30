package com.etr.service;

import com.etr.model.AccessToken;

/**
 * Created by LJW on 2019/5/25 - 22:03
 */
public interface AccessTokenService {
    boolean addAccessToken(AccessToken token);
    boolean updateAccessToken(AccessToken token);
    AccessToken getAccessTokenbyOpenId(String openId);
}

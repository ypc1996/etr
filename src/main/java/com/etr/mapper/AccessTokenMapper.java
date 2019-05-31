package com.etr.mapper;

import com.etr.model.AccessToken;
import org.springframework.stereotype.Service;


/**
 * Created by LJW on 2019/5/25 - 22:00
 */
public interface AccessTokenMapper {
    boolean addAccessToken(AccessToken token);
    boolean updateAccessToken(AccessToken token);
    AccessToken getAccessTokenbyOpenId(String openId);
}

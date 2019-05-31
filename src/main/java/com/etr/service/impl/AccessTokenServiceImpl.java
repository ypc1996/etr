package com.etr.service.impl;

import com.etr.mapper.AccessTokenMapper;
import com.etr.model.AccessToken;
import com.etr.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LJW on 2019/5/25 - 22:03
 */
@Service
public class AccessTokenServiceImpl implements AccessTokenService{

    @Autowired
    private AccessTokenMapper accessTokenMapper;
    @Override
    public boolean addAccessToken(AccessToken token) {
        return accessTokenMapper.addAccessToken(token);
    }

    @Override
    public boolean updateAccessToken(AccessToken token) {
        return accessTokenMapper.updateAccessToken(token);
    }

    @Override
    public AccessToken getAccessTokenbyOpenId(String openId) {
        return accessTokenMapper.getAccessTokenbyOpenId(openId);
    }
}

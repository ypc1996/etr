package com.etr.service.impl;

import com.etr.mapper.UserMapper;
import com.etr.model.User;
import com.etr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by LJW on 2019/5/23 - 21:49
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(String userId) {
        return userMapper.findUserById(userId);

    }

    @Override
    public User getUserInfobyOpenID(String openID) {

        return userMapper.getUserInfobyOpenID(openID);
    }

    @Override
    public boolean addUser(User user) {

        return userMapper.addUser(user);
    }

    @Override
    public void addToken(String token, String openid, String createTime) {

    }
}

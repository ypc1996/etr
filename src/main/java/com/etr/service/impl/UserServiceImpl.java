package com.etr.service.impl;

import com.etr.mapper.UserMapper;
import com.etr.model.User;
import com.etr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
<
 * @Author: ypc
 * @Date: 2019/5/25 10:57
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByOpenId(String openId) {
        return userMapper.findUserByOpenId(openId);
    }

    @Override
    public Integer addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public Integer updateUser(User user) {
        return userMapper.updateUser(user);
    }
}

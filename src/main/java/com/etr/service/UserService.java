package com.etr.service;

import com.etr.model.User;

/**
 * @Author: ypc
 * @Date: 2019/5/25 10:57
 * @Description:
 */
public interface UserService {
    User findUserById(Integer id);
    User findUserByOpenId(String openId);
    Integer addUser(User user);
    Integer updateUser(User user);

}

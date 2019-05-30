package com.etr.service;

import com.etr.model.User;

/**
 * Created by LJW on 2019/5/23 - 21:48
 */
public interface UserService {
    User findUserById(String userId);
    User  getUserInfobyOpenID(String openID);
    boolean addUser(User user);

    void addToken(String token, String openid, String createTime);
}

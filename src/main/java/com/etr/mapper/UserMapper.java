package com.etr.mapper;

import com.etr.model.User;

/**
 * Created by LJW on 2019/5/23 - 21:51
 */
public interface UserMapper {
    User findUserById(String userId);
    User getUserInfobyOpenID(String openID);
    boolean addUser(User user);
}

package com.etr.mapper;

import com.etr.model.User;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: ypc
 * @Date: 2019/5/25 10:54
 * @Description:
 */
public interface UserMapper {
    User findUserById(@Param("id") Integer id);
    User findUserByOpenId(@Param("openId")String openId);
    Integer addUser(User user);
    Integer updateUser(User user);

}

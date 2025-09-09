package com.reggie.service.impl;

import com.reggie.entity.User;
import com.reggie.mapper.UserMapper;
import com.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public int getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    public void save(User user) {
        userMapper.save(user);
    }

    @Override
    public Object getUserID(User user) {
        return userMapper.getUserID(user);
    }
}

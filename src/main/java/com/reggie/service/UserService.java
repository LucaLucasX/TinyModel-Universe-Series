package com.reggie.service;

import com.reggie.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    int getUserByPhone(String user);

    void save(User user);

    Object getUserID(User user);
}

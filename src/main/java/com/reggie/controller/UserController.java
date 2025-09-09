package com.reggie.controller;

import com.reggie.common.R;
import com.reggie.entity.User;
import com.reggie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
public R<User> login(HttpServletRequest request, @RequestBody User user){
        log.info(user.getPhone());
        //判断是否为新用户
 String phone=user.getPhone();
int count=userService.getUserByPhone(phone);
if(count==0){
    userService.save(user);
    request.getSession().setAttribute("user",userService.getUserID(user));
    return R.success(user);
}
if(count>0){
    request.getSession().setAttribute("user",userService.getUserID(user));
    return R.success(user);
}
        return R.error("登录失败，请联系管理员");
    }
}

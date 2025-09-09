package com.reggie.mapper;

import com.reggie.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select count(*) from user where phone=#{phone}")
    int getUserByPhone(String phone);
@Insert("insert into user (phone) values (#{phone})")
    void save(User user);
@Select("select id from user where phone=#{phone}")
    Object getUserID(User user);
}

package com.reggie.mapper;

import com.reggie.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployMapper {
    @Select("select count(*) from employee")
    Long count();

    @Select("select * from employee where username=#{username} ")
    Employee getEmpByusername(String username);
@Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user)values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Employee employee);

    List<Object> list(Integer s, int pageSize, String name);
@Update("update employee set status=#{status},update_time=#{updateTime},username=#{username},name=#{name},phone=#{phone},sex=#{sex},id_number=#{idNumber} where id=#{id}")
    void updateByID(Employee employee);
@Select("select * from employee where id=#{id}")
    Employee getEmpByID(Long id);
@Update("update employee set status=#{status} where id=#{id}")
    void updateStatusByID(Employee employee);
}

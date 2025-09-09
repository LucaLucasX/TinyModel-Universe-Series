package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper{
@Insert("insert into address_book (user_id, consignee, sex, phone,is_default, create_time, update_time, create_user, update_user,is_deleted,detail,label) values (#{userId},#{consignee},#{sex},#{phone},#{isDefault},#{createTime},#{updateTime},#{createUser},#{updateUser},#{isDeleted},#{detail},#{label})")
    void save(AddressBook addressBook);
@Update("update address_book set is_default = 0 where user_id = #{userId}")
    void update(Long userId);
@Select("select id from address_book where user_id=#{userId} and consignee=#{consignee} and sex=#{sex} and phone =#{phone}")
    Long getID(AddressBook addressBook);
@Update("update address_book set is_default = 1 where id = #{id}")
    void updateById(AddressBook addressBook);
@Select("select  * from address_book where id=#{id}")
    AddressBook getById(Long id);
@Select("select * from address_book where user_id =#{userId} and is_default = 1")
    AddressBook getOneBydefault(Long userId);
@Select("select * from address_book where user_id =#{userId} order by update_time desc")
    List<AddressBook> list(Long userId);
}

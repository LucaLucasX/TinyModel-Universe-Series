package com.reggie.mapper;

import com.reggie.common.R;
import com.reggie.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category (sort,type, name, create_time, update_time, create_user, update_user) values (#{sort},#{type},#{name},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Category category);
@Select("select count(*) from category")
    Long count();

    List<Object> list(Integer s, int pageSize);
@Delete("delete from category where id=#{id}")
    void removeByID(Long id);
@Update("update category set sort=#{sort},name=#{name},update_time=#{updateTime},update_user=#{updateUser} where id =#{id}")
    void update(Category category);
@Select("select * from category where type=#{type} order by sort ,update_time desc")
    List<Category> listByCategoryType(Category category);
@Select("select name from category where id=#{categoryId}")
    String getNameById(Long categoryId);
@Select("select * from category")
    List<Category> getAll();
}

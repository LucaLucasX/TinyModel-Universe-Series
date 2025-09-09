package com.reggie.mapper;

import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select count(*) from dish where category_id=#{id}")
    int getCountByCategoryID(Long id);
@Insert("insert into dish (name, category_id, price, code, image, description, status, sort, create_time, update_time, create_user, update_user, is_deleted) values (#{name},#{categoryId},#{price},#{code},#{image},#{description},#{status},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser},#{isDeleted})")
    void save(DishDto dishDto);
@Select("select id from dish where name=#{name}")
Long getIDByCategoryName(DishDto dishDto);
@Select("select count(*) from dish")
    Long count();

    List<DishDto> list(Integer s, int pageSize, String name);
@Select("select * from dish where id=#{id}")
Dish getById(Long id);
@Update("update dish set name=#{name},price=#{price},category_id=#{categoryId},image=#{image},update_time=#{updateTime},update_user=#{updateUser},description=#{description} where id=#{id}")
    void update(DishDto dishDto);
@Select("select * from dish where category_id=#{categoryId} and dish.status=1 order by sort desc,update_time desc")
    List<Dish> getDishByCategoryId(Long categoryId);
}

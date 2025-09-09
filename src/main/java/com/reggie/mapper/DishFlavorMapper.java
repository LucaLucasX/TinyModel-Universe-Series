package com.reggie.mapper;

import com.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    @Insert(" insert into dish_flavor(dish_id,name,value,create_time,update_time,create_user,update_user,is_deleted) values (#{dishId},#{name},#{value},#{createTime},#{updateTime},#{createUser},#{updateUser},#{isDeleted})")
    void saveFlavor(DishFlavor flavor);
@Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getDishFlavorByDishId(Long id);
@Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteFlavorByDishId(Long id);
}

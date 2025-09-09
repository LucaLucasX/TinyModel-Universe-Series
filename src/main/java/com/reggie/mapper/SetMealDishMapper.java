package com.reggie.mapper;

import com.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies, sort, create_time, update_time, create_user, update_user, is_deleted) values (#{setmealId},#{dishId},#{name},#{price},#{copies},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser},#{isDeleted})")
    void savemealDish(SetmealDish setmealDish);

    void delete(List<Long> ids);
@Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getDishesBysetmealID(Long id);
}

package com.reggie.mapper;

import com.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

@Select("select * from shopping_cart where dish_id=#{dishId} and user_id=#{userId}")
    ShoppingCart getNumberByFDUId(ShoppingCart shoppingCart);
@Select("select id from shopping_cart where dish_id=#{dishId} and user_id=#{userId}")
    Integer getID(ShoppingCartMapper shoppingCartMapper);
@Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(Long id, int number);
@Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number,amount, create_time) values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);
@Select("select * from shopping_cart where setmeal_id=#{setmealId} and user_id=#{userId}")
    ShoppingCart getNumBySUId(ShoppingCart shoppingCart);
    @Select("select id from shopping_cart where shopping_cart.setmeal_id=#{dishId} and user_id=#{userId}")
    Integer getSetMealID(ShoppingCart shoppingCart);
@Select("select * from shopping_cart where user_id=#{userID}")
    List<ShoppingCart> getAllByUserId(Long userID);
@Delete("delete from shopping_cart where user_id=#{userID}")
    void delete(Long userID);
}

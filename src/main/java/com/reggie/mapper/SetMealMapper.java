package com.reggie.mapper;

import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {
    @Select("select count(*) from setmeal where category_id=#{id}")
    int getCountByCategoryID(Long id);
@Insert("insert into setmeal (category_id, name, price, status, code, description, image, create_time, update_time, create_user, update_user) values (#{categoryId},#{name},#{price},#{status},#{code},#{description},#{image},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(SetmealDto setmealDto);
@Select("select id from setmeal where name=#{name}")
    Long getIdByName(String name);
@Select("select count(*) from setmeal")
    Long count();

    List<SetmealDto> list(Integer s, int pageSize, String name);
@Select("select count(*) from setmeal where status=1 and id=#{id}")
    int countByStatus(long id);

    void delete(List<Long> ids);
@Select("select * from setmeal where category_id=#{categoryId}")
    List<Setmeal> getAllByCategoryID(Long categoryId);
@Select("select * from setmeal where id=#{id}")
    Setmeal getByID(Long id);
}

package com.reggie.service.impl;

import com.reggie.common.CustomException;
import com.reggie.common.PageBean;
import com.reggie.dto.DishDto;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;
import com.reggie.mapper.CategoryMapper;
import com.reggie.mapper.SetMealDishMapper;
import com.reggie.mapper.SetMealMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl implements com.reggie.service.SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    public int getCountByCategoryID(Long id) {
        return setMealMapper.getCountByCategoryID(id);
    }
@Autowired
private SetMealDishMapper setMealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    //保存套餐及其菜品
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
//保存套餐信息
setMealMapper.save(setmealDto);
        //菜品信息赋值
List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes();
for (int i=0;i<setmealDishList.size();i++){
    Long Id = setmealDto.getCreateUser();
    setmealDishList.get(i).setSetmealId(setMealMapper.getIdByName(setmealDto.getName()));
    setmealDishList.get(i).setCreateTime(LocalDateTime.now());
    setmealDishList.get(i).setCreateUser(Id);
    setmealDishList.get(i).setUpdateTime(LocalDateTime.now());
    setmealDishList.get(i).setUpdateUser(Id);
    setmealDishList.get(i).setIsDeleted(0);
    setmealDishList.get(i).setSort(0);
    setMealDishMapper.savemealDish(setmealDishList.get(i));
}
    }

    @Override
    public PageBean page(int page, int pageSize, String name) {
        //1、获取总记录数
        Long count = setMealMapper.count();

        //2、获取分页查询结果列表
        Integer S = (page - 1) * pageSize; //计算起始索引 , 公式: (页码-1)*页大小
        List<SetmealDto> setmealDtos = setMealMapper.list(S, pageSize, name);
        List<Object> objectList=new ArrayList<>();
        for (int i = 0; i <  setmealDtos.size(); i++) {
            setmealDtos.get(i).setCategoryName(categoryMapper.getNameById( setmealDtos.get(i).getCategoryId()));
            objectList.add( setmealDtos.get(i));
        }
        //3、封装PageBean对象
        return new PageBean(objectList,count);
    }
@Transactional
    @Override
    public void delete(List<Long> ids) {
        //判断套餐是否启用
    int count=0;
    for(int i =0;i<ids.size();i++){
    count+=setMealMapper.countByStatus(i);
    }
    log.info("数量为：{}",count);
        //启用则抛出异常
    if(count>0){
        throw new CustomException("套餐正在售卖中,不能删除");
    }
        //先删除套餐表中的数据
    setMealMapper.delete(ids);
    //在删除关系表setmeal_dish中的数据
setMealDishMapper.delete(ids);
    }

    @Override
    public List<Setmeal> getAllByCategoryID(Long categoryId) {
        return setMealMapper.getAllByCategoryID(categoryId);
    }

    @Override
    public Setmeal getByID(Long id) {
        return setMealMapper.getByID(id);
    }
}

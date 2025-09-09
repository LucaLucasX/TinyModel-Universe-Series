package com.reggie.service.impl;

import com.reggie.common.PageBean;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.CategoryMapper;
import com.reggie.mapper.DishFlavorMapper;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
@Autowired
private CategoryMapper categoryMapper;
    @Override
    public int getCountByCategoryID(Long id) {
        return dishMapper.getCountByCategoryID(id);
    }

    //多张表控制，事务控制
    @Transactional
    @Override
    public void save(DishDto dishDto) {
        //先保存菜品的基本信息
        dishMapper.save(dishDto);
        Long id = dishMapper.getIDByCategoryName(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        //将每一个表加入id
        flavors = flavors.stream().peek((item) -> item.setDishId(id)).toList();
        //遍历集合
        for (int i = 0; i < flavors.size(); i++) {
            Long Id = dishDto.getCreateUser();
            flavors.get(i).setCreateTime(LocalDateTime.now());
            flavors.get(i).setCreateUser(Id);
            flavors.get(i).setUpdateTime(LocalDateTime.now());
            flavors.get(i).setUpdateUser(Id);
            flavors.get(i).setIsDeleted(0);
            dishFlavorMapper.saveFlavor(flavors.get(i));
        }
    }

    @Override
    public PageBean page(int page, int pageSize, String name) {
        //1、获取总记录数
        Long count = dishMapper.count();

        //2、获取分页查询结果列表
        Integer S = (page - 1) * pageSize; //计算起始索引 , 公式: (页码-1)*页大小
        List<DishDto> empList = dishMapper.list(S, pageSize, name);
        List<Object> objectList=new ArrayList<>();
        for (int i = 0; i < empList.size(); i++) {
                  empList.get(i).setCategoryName(categoryMapper.getNameById(empList.get(i).getCategoryId()));
                  objectList.add(empList.get(i));
        }
        //3、封装PageBean对象
        return new PageBean(objectList,count);
    }

    @Override
    public Dish getDishById(Long id) {
        return dishMapper.getById(id);
    }
    @Transactional
    //即更新菜品信息也更新口味信息
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        dishMapper.update(dishDto);
//先将对应口味清空
dishFlavorMapper.deleteFlavorByDishId(dishDto.getId());
        //更新口味
        for (int i=0;i<dishDto.getFlavors().size();i++){
            Long Id = dishDto.getCreateUser();
            dishDto.getFlavors().get(i).setCreateTime(LocalDateTime.now());
            dishDto.getFlavors().get(i).setCreateUser(Id);
            dishDto.getFlavors().get(i).setDishId(dishMapper.getIDByCategoryName(dishDto));
            dishDto.getFlavors().get(i).setUpdateTime(LocalDateTime.now());
            dishDto.getFlavors().get(i).setUpdateUser(Id);
            dishDto.getFlavors().get(i).setIsDeleted(0);
            dishFlavorMapper.saveFlavor(dishDto.getFlavors().get(i));
        }
    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }
}

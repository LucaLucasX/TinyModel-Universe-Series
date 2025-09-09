package com.reggie.service.impl;

import com.reggie.common.CustomException;
import com.reggie.common.PageBean;
import com.reggie.entity.Category;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealServiceImpl service;
    @Override
    public void save(Category category) {
        categoryMapper.save(category);
    }

    @Override
    public PageBean page(int page, int pageSize) {
        //1、获取总记录数
        Long count = categoryMapper.count();

        //2、获取分页查询结果列表
        Integer S = (page - 1) * pageSize; //计算起始索引 , 公式: (页码-1)*页大小
        List<Object> empList =categoryMapper.list(S, pageSize);

        //3、封装PageBean对象
        return new PageBean(empList,count);
    }

    @Override
    public void removeByID(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
int count1 =dishService.getCountByCategoryID(id);
log.info("关联菜品数：{}",count1);
if(count1>0){
    //已经关联
    throw new CustomException("当前分类下关联了菜品，不能删除");
}
        //套餐同理
        int count2=service.getCountByCategoryID(id);
if(count2>0){
    //已经关联
    throw new CustomException("当前分类下关联了套餐，不能删除");
}
        categoryMapper.removeByID(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public List<Category> listByCategoryType(Category category) {
        return categoryMapper.listByCategoryType(category);
    }

    @Override
    public List<Category> list() {
        return categoryMapper.getAll();
    }
}

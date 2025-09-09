package com.reggie.service.impl;

import com.reggie.entity.SetmealDish;
import com.reggie.mapper.SetMealDishMapper;
import com.reggie.service.SetMealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealDishServiceImpl implements SetMealDishService {
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Override
    public List<SetmealDish> getDishesBysetmealID(Long id) {
        return setMealDishMapper.getDishesBysetmealID(id);
    }
}

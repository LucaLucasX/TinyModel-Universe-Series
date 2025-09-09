package com.reggie.service;

import com.reggie.entity.SetmealDish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetMealDishService {
    List<SetmealDish> getDishesBysetmealID(Long id);
}

package com.reggie.service;

import com.reggie.common.PageBean;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishService {
    int getCountByCategoryID(Long id);

    void save(DishDto dishDto);

    PageBean page(int page, int pageSize, String name);

    Dish getDishById(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<Dish> getDishByCategoryId(Long categoryId);
}

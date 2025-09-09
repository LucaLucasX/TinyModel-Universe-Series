package com.reggie.service;

import com.reggie.common.PageBean;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetMealService {
    void saveWithDish(SetmealDto setmealDto);

    PageBean page(int page, int pageSize, String name);

    void delete(List<Long> ids);

    List<Setmeal> getAllByCategoryID(Long categoryId);

    Setmeal getByID(Long id);
}

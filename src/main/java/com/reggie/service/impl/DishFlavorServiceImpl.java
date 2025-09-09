package com.reggie.service.impl;

import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishFlavorMapper;
import com.reggie.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishFlavorServiceImpl implements DishFlavorService {
  @Autowired
  private DishFlavorMapper dishFlavorMapper;
    @Override
    public List<DishFlavor> getDishFlavorByDishId(Long id) {
        return dishFlavorMapper.getDishFlavorByDishId(id);
    }
}

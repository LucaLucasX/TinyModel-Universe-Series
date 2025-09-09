package com.reggie.service;

import com.reggie.entity.DishFlavor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishFlavorService {
    List<DishFlavor> getDishFlavorByDishId(Long id);
}

package com.reggie.controller;

import com.reggie.common.PageBean;
import com.reggie.common.R;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        Long id = (long) request.getSession().getAttribute("employee");
        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setCreateUser(id);
        dishDto.setUpdateTime(LocalDateTime.now());
        dishDto.setUpdateUser(id);
        dishDto.setSort(0);
        dishDto.setIsDeleted(0);
        log.info(dishDto.toString());
        dishService.save(dishDto);
        return R.success("添加菜品成功");
    }

    @GetMapping("/page")
    public R<PageBean> pageBeanR(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name= {}", page, pageSize, name);
        PageBean pageBean = dishService.page(page, pageSize, name);
        // log.info("pageBena is {}",pageBean);
        return R.success(pageBean);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        log.info("id为：{}", id);
        //通过id构造DishDto对象
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getDishById(id);
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setCategoryName(categoryMapper.getNameById(dish.getCategoryId()));
        List<DishFlavor> dishFlavorList = dishFlavorService.getDishFlavorByDishId(id);
        dishDto.setFlavors(dishFlavorList);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        Long id = (long) request.getSession().getAttribute("employee");
        dishDto.setUpdateTime(LocalDateTime.now());
        dishDto.setUpdateUser(id);
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<Dish> dishes = dishService.getDishByCategoryId(dish.getCategoryId());
        List<DishDto> dishDtos=new ArrayList<>();
        for(int i=0;i<dishes.size();i++){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dishes.get(i),dishDto);
            dishDto.setFlavors(dishFlavorService.getDishFlavorByDishId(dishes.get(i).getId()));
            dishDto.setCategoryName(categoryMapper.getNameById(dishes.get(i).getCategoryId()));
            dishDtos.add(dishDto);
        }
        return R.success(dishDtos);
    }
}

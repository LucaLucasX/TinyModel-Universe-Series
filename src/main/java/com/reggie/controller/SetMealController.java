package com.reggie.controller;

import com.reggie.common.PageBean;
import com.reggie.common.R;
import com.reggie.dto.DishDto;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;
import com.reggie.mapper.CategoryMapper;
import com.reggie.mapper.DishMapper;
import com.reggie.service.SetMealDishService;
import com.reggie.service.SetMealService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealDto.setCategoryName(categoryMapper.getNameById(setmealDto.getCategoryId()));
        Long id = (long) request.getSession().getAttribute("employee");
        setmealDto.setCreateTime(LocalDateTime.now());
        setmealDto.setCreateUser(id);
        setmealDto.setUpdateTime(LocalDateTime.now());
        setmealDto.setUpdateUser(id);
        setmealDto.setIsDeleted(0);
        setMealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功");
    }

    @GetMapping("/page")
    public R<PageBean> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name= {}", page, pageSize, name);
        PageBean pageBean = setMealService.page(page, pageSize, name);
        // log.info("pageBena is {}",pageBean);
        return R.success(pageBean);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除的套餐id为：{}", ids);
        setMealService.delete(ids);
        return R.success("套餐删除成功");
    }

    @GetMapping("/list")
    public R<List<SetmealDto>> getDishBySetMeal(Setmeal setmeal) {
        List<SetmealDto> setmealDishes = new ArrayList<>();
        List<Setmeal> setmeals = setMealService.getAllByCategoryID(setmeal.getCategoryId());
        for (int i = 0; i < setmeals.size(); i++) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeals.get(i), setmealDto);
            setmealDto.setSetmealDishes(setMealDishService.getDishesBysetmealID(setmeals.get(i).getId()));
            setmealDto.setCategoryName(categoryMapper.getNameById(setmeal.getCategoryId()));
            setmealDishes.add(setmealDto);
        }
        return R.success(setmealDishes);
    }
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> getSetmealDishes(@PathVariable Long id){
        log.info("id为：{}",id);
        List<DishDto>dishes=new ArrayList<>();
        List<SetmealDish>setmealDishes=setMealDishService.getDishesBysetmealID(id);
        for(int i=0;i<setmealDishes.size();i++){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dishMapper.getById(setmealDishes.get(i).getId()),dishDto);
            dishDto.setCopies(setmealDishes.get(i).getCopies());
            dishes.add(dishDto);
        }
        return R.success(dishes);
    }
}

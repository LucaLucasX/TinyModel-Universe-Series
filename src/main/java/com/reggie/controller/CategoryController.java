package com.reggie.controller;

import com.reggie.common.PageBean;
import com.reggie.common.R;
import com.reggie.entity.Category;
import com.reggie.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category) {
        log.info("category：{}", category);
        Long id = (long) request.getSession().getAttribute("employee");
        category.setCreateTime(LocalDateTime.now());
        category.setCreateUser(id);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(id);
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<PageBean> page(int page,int pageSize){
        log.info("page = {},pageSize = {}",page,pageSize);
        PageBean pageBean = categoryService.page(page,pageSize);
        // log.info("pageBena is {}",pageBean);
        return R.success(pageBean);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类,id为：{}",ids);
        categoryService.removeByID(ids);
        return R.success("删除分类成功");
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Category category){
        log.info("更新分类为：{}",category);
        Long id = (long) request.getSession().getAttribute("employee");
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(id);
        categoryService.update(category);
        return R.success("更新成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        List<Category> categories=new ArrayList<>();
        if(category.getType()==null)categories=categoryService.list();
        else categoryService.listByCategoryType(category);
        return R.success(categories);
    }
}

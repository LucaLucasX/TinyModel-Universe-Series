package com.reggie.service;

import com.reggie.common.PageBean;
import com.reggie.common.R;
import com.reggie.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    void save(Category category);

    PageBean page(int page, int pageSize);

    void removeByID(Long id);

    void update(Category category);

    List<Category> listByCategoryType(Category category);

    List<Category> list();
}

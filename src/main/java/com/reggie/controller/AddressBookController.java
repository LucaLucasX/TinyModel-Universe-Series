package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.reggie.common.R;
import com.reggie.entity.AddressBook;
import com.reggie.service.AddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long id= (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(id);
        addressBook.setCreateUser(id);
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        addressBook.setUpdateUser(id);
        addressBook.setIsDeleted(0);
        addressBook.setIsDefault(0);
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(HttpServletRequest request,@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        AddressBook addressBook1=addressBookService.getById(addressBook.getId());
        Long userId= (Long) request.getSession().getAttribute("user");
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(userId);
        //获取当前地址信息id
        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault(HttpServletRequest request) {
        Long userId= (Long) request.getSession().getAttribute("ufser");
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOneBydefault(userId);
        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request,AddressBook addressBook) {
        addressBook.setUserId((Long) request.getSession().getAttribute("user"));
        log.info("addressBook:{}", addressBook);
        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list((Long) request.getSession().getAttribute("user")));
    }
}

package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService{

    void save(AddressBook addressBook);

    void update(Long userId);

    Long getID(AddressBook addressBook);

    void updateById(AddressBook addressBook);

    AddressBook getById(Long id);

    AddressBook getOneBydefault(Long userId);

    List<AddressBook> list(Long userId);
}

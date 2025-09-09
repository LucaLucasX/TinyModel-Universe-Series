package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.entity.AddressBook;
import com.reggie.mapper.AddressBookMapper;
import com.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
@Autowired
private AddressBookMapper addressBookMapper;
    @Override
    public void save(AddressBook addressBook) {
        addressBookMapper.save(addressBook);
    }

    @Override
    public void update(Long userId) {
        addressBookMapper.update(userId);
    }

    @Override
    public Long getID(AddressBook addressBook) {
        return addressBookMapper.getID(addressBook);
    }

    @Override
    public void updateById(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    public AddressBook getOneBydefault(Long userId) {
        return addressBookMapper.getOneBydefault(userId);
    }

    @Override
    public List<AddressBook> list(Long userId) {
        return addressBookMapper.list(userId);
    }
}

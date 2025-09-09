package com.reggie.service.impl;

import com.reggie.entity.ShoppingCart;
import com.reggie.mapper.ShoppingCartMapper;
import com.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart getNumberByFDUId(ShoppingCart shoppingCart) {
        return shoppingCartMapper.getNumberByFDUId(shoppingCart);
    }

    @Override
    public Integer getID(ShoppingCart shoppingCart) {
        return shoppingCartMapper.getID(shoppingCartMapper);
    }

    @Override
    public void updateNumberById(Long id, int number) {
shoppingCartMapper.updateNumberById(id,number);
    }

    @Override
    public void insert(ShoppingCart shoppingCart) {
        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public ShoppingCart getNumberBySUId(ShoppingCart shoppingCart) {
        return shoppingCartMapper.getNumBySUId(shoppingCart);
    }

    @Override
    public Integer getSetMealID(ShoppingCart shoppingCart) {
        return shoppingCartMapper.getSetMealID(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getAllByUserId(Long userID) {
        return shoppingCartMapper.getAllByUserId(userID);
    }

    @Override
    public void delete(Long userID) {
        shoppingCartMapper.delete(userID);
    }
}

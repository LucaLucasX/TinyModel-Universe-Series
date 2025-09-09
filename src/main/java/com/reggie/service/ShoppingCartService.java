package com.reggie.service;

import com.reggie.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {

    ShoppingCart getNumberByFDUId(ShoppingCart shoppingCart);

    Integer getID(ShoppingCart shoppingCart);

    void updateNumberById(Long id, int number);

    void insert(ShoppingCart shoppingCart);

    ShoppingCart getNumberBySUId(ShoppingCart shoppingCart);

    Integer getSetMealID(ShoppingCart shoppingCart);

    List<ShoppingCart> getAllByUserId(Long userID);

    void delete(Long userID);
}

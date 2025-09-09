package com.reggie.controller;

import com.reggie.common.R;
import com.reggie.entity.ShoppingCart;
import com.reggie.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(HttpServletRequest request, @RequestBody ShoppingCart shoppingCart) {
        //设置当前用户ID与更新时间
        Long userID = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userID);
        shoppingCart.setCreateTime(LocalDateTime.now());
        log.info("加入物品为：{}", shoppingCart);
        //先判断加入的是菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            //为菜
            //再查看购物车中是否已存在（通过口味与dishID与userID共同判断）
            ShoppingCart shoppingCart1 = shoppingCartService.getNumberByFDUId(shoppingCart);
            if (shoppingCart1!=null) {
                //存在，只更新数量
                Long ID =shoppingCart1.getId();
                shoppingCart.setId(shoppingCart1.getId());
                shoppingCart.setNumber(shoppingCart1.getNumber()+1);
                shoppingCartService.updateNumberById(ID, shoppingCart1.getNumber()+1);
            } else {
                //不存在，插入
                shoppingCart.setNumber(1);
                shoppingCartService.insert(shoppingCart);
            }
        } else {
            //为套餐
            ShoppingCart shoppingCart1 = shoppingCartService.getNumberBySUId(shoppingCart);
            if (shoppingCart1!= null) {
                //存在，只更新数量
                Long ID = shoppingCart1.getId();
                shoppingCart.setId(shoppingCart1.getId());
                shoppingCart.setNumber(shoppingCart1.getNumber()+1);
                shoppingCartService.updateNumberById(ID, shoppingCart1.getNumber()+1);
            } else {
                //不存在，插入
                shoppingCart.setNumber(1);
                shoppingCartService.insert(shoppingCart);
            }
        }
        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpServletRequest request) {
        Long userID = (Long) request.getSession().getAttribute("user");
        return R.success(shoppingCartService.getAllByUserId(userID));
    }
    @DeleteMapping("/clean")
    public R<String> clean(HttpServletRequest request){
        Long userID = (Long) request.getSession().getAttribute("user");
        shoppingCartService.delete(userID);
        return R.success("购物车清空成功");
    }
}

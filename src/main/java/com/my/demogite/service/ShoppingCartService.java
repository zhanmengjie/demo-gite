package com.my.demogite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.demogite.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    public List<ShoppingCart> showFlavorAndSetmeal(ShoppingCart shoppingCart);
}

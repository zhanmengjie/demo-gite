package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.common.R;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.DishFlavor;
import com.my.demogite.entity.ShoppingCart;
import com.my.demogite.mapper.ShoppingCartMapper;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import com.my.demogite.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Override
    public List<ShoppingCart> showFlavorAndSetmeal(ShoppingCart shoppingCart) {
//        //首先获取通过菜品的id和状态获取菜品信息，;
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dish::getId,shoppingCart.getId()).eq(Dish::getStatus,1);
//        Dish one1 = dishService.getOne(queryWrapper);
//        //然后获取到口味信息
//        LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
//        queryWrapper1.eq(DishFlavor::getDishId,shoppingCart.getDishId());
//        DishFlavor one = dishFlavorService.getOne(queryWrapper1);
//        BeanUtils.copyProperties(one1, one);
//        return R.success(one);
        return null;
    }
}

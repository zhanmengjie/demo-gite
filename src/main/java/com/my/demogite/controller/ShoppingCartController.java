package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.demogite.common.BaseContext;
import com.my.demogite.common.R;
import com.my.demogite.entity.ShoppingCart;
import com.my.demogite.service.CategoryService;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import com.my.demogite.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@SuppressWarnings("all")
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingcart : {}",shoppingCart.toString());
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品，或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null) {
            //添加购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //如果以及存在则在原来数量基础上加一
        ShoppingCart shoppingCartone = shoppingCartService.getOne(queryWrapper);
        if (shoppingCartone != null) {
            //如果以及存在，在原先的基础数量上加一
            Integer number = shoppingCartone.getNumber();
            shoppingCartone.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCartone);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCartone = shoppingCart;
        }


        return R.success(shoppingCartone);
    }
    /**
     * 显示购物车信息
     * @param shoppingCart
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> showShopCart(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 删除购物车 中的一个商品
     */
    @PostMapping("/sub")
    public R<ShoppingCart> deleteCart(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null) {
            //添加购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartTwo = shoppingCartService.getOne(queryWrapper);

        if (shoppingCartTwo != null) {
            Integer number = shoppingCartTwo.getNumber();
            shoppingCartTwo.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartTwo);
        }else {
            shoppingCart.setNumber(0);
            shoppingCartService.remove(queryWrapper);
            shoppingCartTwo = shoppingCart;
        }

        return R.success(shoppingCartTwo);
    }
    /**
     * 删除当前用户的全部的购物车中的信息
     */
    @DeleteMapping("/clean")
    public R<String> deleteCartAll(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车全部删除成功");
    }

}

package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.demogite.common.BaseContext;
import com.my.demogite.common.R;
import com.my.demogite.entity.Category;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.DishFlavor;
import com.my.demogite.entity.ShoppingCart;
import com.my.demogite.entity.dto.DishDto;
import com.my.demogite.service.CategoryService;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import com.my.demogite.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    /**
     * 显示菜品信息
     * @param shoppingCart
     * @return
     */
//    @GetMapping("/list")
//    public R<List<DishDto>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        List<DishDto> dishDtoList = list.stream().map((item) -> {
//            DishDto dishDto = new DishDto();
//            //将item的数据，拷贝到给我们刚创建的dishDto对象
//            BeanUtils.copyProperties(item, dishDto);
//            //获取菜品分类的的id
//            Long categoryId = item.getCategoryId();
//            //根据菜品分类的id ，查询菜品分类的名称
//            Category category = categoryService.getById(categoryId);
//            if (category != null) {
//                //查询菜品分类的名称
//                String name1 = category.getName();
//                dishDto.setCategoryName(name1);
//            }
//            /**
//             * 菜品id
//             */
//            Long dishId = item.getId();
//            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(DishFlavor::getDishId,dishId);
//
//            //口味集合
//            List<DishFlavor> list1 = dishFlavorService.list(wrapper);
//            dishDto.setFlavors(list1);
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        return R.success(dishDtoList);
//    }
    //添加购物车，选择对应的菜品口味
    @PostMapping("/add")
    public R<String> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingcart : {}",shoppingCart.toString());
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        shoppingCartService.save(shoppingCart);
        return R.success("添加购物车成功!");
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> showShopCart(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    /**
     * 删除购物车 中的一个商品
     */
    @PostMapping("/sub")
    public R<String> deleteCart(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId,dishId).eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("删除单个购车中的信息");
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

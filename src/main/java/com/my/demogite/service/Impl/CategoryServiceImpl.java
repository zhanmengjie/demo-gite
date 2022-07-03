package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.common.CustomException;
import com.my.demogite.entity.Category;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.Setmeal;
import com.my.demogite.mapper.CategoryMapper;
import com.my.demogite.service.CategoryService;
import com.my.demogite.service.DishService;
import com.my.demogite.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果关联，抛出业务异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);
        if (count > 0) {
            //查询当前分类是否关联了菜品，如果关联，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能进行删除");
        }
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(wrapper);
        if (count1 > 0) {
            //查询当前分类是否关联了套餐，如果关联，抛出一个异常
            throw new CustomException("当前分类下关联了套餐，不能进行删除");
        }
       super.removeById(id);
        //来删除分类
    }
}

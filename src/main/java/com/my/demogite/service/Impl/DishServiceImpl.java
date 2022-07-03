package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.DishFlavor;
import com.my.demogite.entity.dto.DishDto;
import com.my.demogite.mapper.DishMapper;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     *   新增菜品，，，，，，然后 对口味表，插入数据   。同时插入俩张表
     */
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本的信息
        this.save(dishDto);
//        new DishServiceImpl().save();
        //菜品id
        Long dtoId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dtoId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味表到dish_flavor表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void deleteWithFlavor(Long id) {
        this.removeById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);
    }

}

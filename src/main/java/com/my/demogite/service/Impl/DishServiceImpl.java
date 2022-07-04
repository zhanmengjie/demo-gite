package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.DishFlavor;
import com.my.demogite.entity.dto.DishDto;
import com.my.demogite.mapper.DishMapper;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     *   新增菜品，，，，，，然后 对口味表，插入数据   。同时插入俩张表
     */
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
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
    public void deleteWithFlavor(Long[] id) {
        List<Long> idList = Arrays.asList(id);
        if (idList == null) {
            return;
        }
        this.removeByIds(idList);
       for ( int i= 0; i < idList.size(); i++ ){
           LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(DishFlavor::getDishId,id);
           dishFlavorService.remove(queryWrapper);
        }
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        /**
         * 查询菜品基本信息
         */
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //拷贝普通的属性
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId() );
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        //然后再将查询到的口味赋值进去
        dishDto.setFlavors(list);
        /**
         * 查询菜品口味信息
         */
        return dishDto;
    }


    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //首先更新菜品
        this.updateById(dishDto);
        //清理当前菜品对应的口味 dish_flavor的delete的删除操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味  dish_flavor的新增操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味表到dish_flavor表
        dishFlavorService.saveBatch(flavors);
//        dishDto.setFlavors(flavors);
    }

    @Override
    public void updateStatus(Long[] ids,int status) {
        List<Long> idList = Arrays.asList(ids);
        if (idList == null) {
            return;
        }
        for (int i = 0; i < idList.size(); i++) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId,idList.get(i)).set(Dish::getStatus,status);
            this.update(updateWrapper);
        }

    }


}

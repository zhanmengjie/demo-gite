package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.common.CustomException;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.Setmeal;
import com.my.demogite.entity.SetmealDish;
import com.my.demogite.entity.dto.SetmealDto;
import com.my.demogite.mapper.SetmealMapper;
import com.my.demogite.service.SetmealDishService;
import com.my.demogite.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，并且同时保存套餐和菜品信息 的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //首先保存套餐信息  setmeal表
        this.save(setmealDto);
        //然后保存套餐 关联的菜品信息  setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //首先查询套餐表套餐的状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).
                eq(Setmeal::getStatus,1 );
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");

        }


        //如果不能删除，直接抛出业务信息

        //首先删除套餐
        this.removeByIds(ids);

        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getDishId,ids);
        setmealDishService.remove(queryWrapper1);

    }

    @Override
    public void updateStatus(Long[] ids, int status) {
        List<Long> idList = Arrays.asList(ids);
        if (idList == null) {
            return;
        }
        for (int i = 0; i < idList.size(); i++) {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId,idList.get(i)).set(Setmeal::getStatus,status);
            this.update(updateWrapper);
        }

    }
}

package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.SetmealDish;
import com.my.demogite.mapper.SetmealDishMapper;
import com.my.demogite.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>implements SetmealDishService {
}

package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.Dish;
import com.my.demogite.mapper.DishMapper;
import com.my.demogite.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}

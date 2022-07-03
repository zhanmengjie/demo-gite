package com.my.demogite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.dto.DishDto;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);
    void deleteWithFlavor(Long id);
}

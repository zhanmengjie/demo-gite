package com.my.demogite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.dto.DishDto;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);
    void deleteWithFlavor(Long[] id);

    /**
     * 根据id 查询菜品口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);


    void updateStatus(Long[] ids,int status);
}

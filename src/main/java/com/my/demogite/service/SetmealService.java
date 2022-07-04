package com.my.demogite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.demogite.entity.Setmeal;
import com.my.demogite.entity.dto.SetmealDto;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，并且同时保存套餐和菜品信息 的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
    public void deleteWithDish(List<Long> ids);

    void updateStatus(Long[] ids, int status);
}

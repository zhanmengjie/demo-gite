package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demogite.common.R;
import com.my.demogite.entity.Category;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.dto.DishDto;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 菜品信息分页功能
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        //构造条件分页
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        wrapper.like(StringUtils.isNotBlank(name),Dish::getName,name);
        wrapper.orderByDesc(Dish::getCreateTime);
        dishService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    /**
     * 新建菜单
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功!");
    }
}

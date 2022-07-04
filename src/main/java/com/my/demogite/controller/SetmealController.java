package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demogite.common.R;
import com.my.demogite.entity.Category;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.Setmeal;
import com.my.demogite.entity.dto.SetmealDto;
import com.my.demogite.service.CategoryService;
import com.my.demogite.service.SetmealDishService;
import com.my.demogite.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> pageDto = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        wrapper.like(StringUtils.isNotBlank(name), Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //获取分类的id
            Long categoryId = item.getCategoryId();
            //根据分类id 获取到信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        pageDto.setRecords(list);


        return R.success(pageDto);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功!!");
    }

    /**
     *
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("封装的数据 {}" ,ids.toString());
        setmealService.deleteWithDish(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 批量修改 菜品状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,Long[] ids) {
        log.info(ids.toString());
        setmealService.updateStatus(ids,status);
        return R.success("菜品状态修改成功!!!");
    }

}

package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demogite.common.R;
import com.my.demogite.entity.Category;
import com.my.demogite.entity.Dish;
import com.my.demogite.entity.DishFlavor;
import com.my.demogite.entity.dto.DishDto;
import com.my.demogite.service.CategoryService;
import com.my.demogite.service.DishFlavorService;
import com.my.demogite.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品信息分页功能
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造条件分页
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> page1 = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        wrapper.like(StringUtils.isNotBlank(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, wrapper);

        /**
         *  对象拷贝
         */
        BeanUtils.copyProperties(pageInfo, page1, "records");
        List<Dish> records = pageInfo.getRecords();
        //遍历records集合  item == records
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将item的数据，拷贝到给我们刚创建的dishDto对象
            BeanUtils.copyProperties(item, dishDto);
            //获取菜品分类的的id
            Long categoryId = item.getCategoryId();
            //根据菜品分类的id ，查询菜品分类的名称
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //查询菜品分类的名称
                String name1 = category.getName();
                dishDto.setCategoryName(name1);
            }

            return dishDto;
        }).collect(Collectors.toList());

        page1.setRecords(list);
        return R.success(page1);
    }

    /**
     * 新建菜单
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 批量删除  菜单
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功!");
    }

    /**
     * 修改菜品，首先要 根据id查询 菜品的信息，并显示出来
     */
    @GetMapping("/{id}")
    public R<DishDto> updateAndSelect(@PathVariable Long id) {
        DishDto flavor = dishService.getByIdWithFlavor(id);
        return R.success(flavor);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 修改菜品  状态（起售，停售）
     * @param status
     * @param ids
     * @return
     */

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,Long[] ids) {
        log.info(ids.toString());
        dishService.updateStatus(ids,status);
        return R.success("菜品状态修改成功!!!");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将item的数据，拷贝到给我们刚创建的dishDto对象
            BeanUtils.copyProperties(item, dishDto);
            //获取菜品分类的的id
            Long categoryId = item.getCategoryId();
            //根据菜品分类的id ，查询菜品分类的名称
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //查询菜品分类的名称
                String name1 = category.getName();
                dishDto.setCategoryName(name1);
            }
            /**
             * 菜品id
             */
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId,dishId);

            //口味集合
            List<DishFlavor> list1 = dishFlavorService.list(wrapper);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}

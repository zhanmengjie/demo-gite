package com.my.demogite.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demogite.common.BaseContext;
import com.my.demogite.common.R;
import com.my.demogite.entity.Orders;

import com.my.demogite.entity.User;
import com.my.demogite.entity.dto.OrdersDto;
import com.my.demogite.service.OrdersService;
import com.my.demogite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    /**
     * 提交订单,
     */
    @PostMapping("/submit")
    public R<String> sumbit(@RequestBody Orders orders){
        log.info("订单数据: {}",orders);
        ordersService.submit(orders);
        return R.success("提交订单成功");
    }
    /**
     * 展示订单
     */
    @GetMapping("/userPage")
    public R<Page> showOrders(int page,int pageSize){
        Long currentId = BaseContext.getCurrentId();
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,currentId).eq(Orders::getStatus,2);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @GetMapping("/page")
    public R<Page> showOrdersHoutai(int page, int pageSize, Long number,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endTime){
        log.info("=== {}",beginTime);
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> page1 = new Page<>(page,pageSize);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format = dateFormat.format(beginTime);
//        String format1 = dateFormat.format(endTime);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null,Orders::getNumber,number);

        queryWrapper.between(beginTime != null && endTime != null,Orders::getCheckoutTime,beginTime,endTime);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(pageInfo,queryWrapper);

        /**
         *  对象拷贝
         */
        BeanUtils.copyProperties(pageInfo, page1, "records");
        List<Orders> records = pageInfo.getRecords();
        //遍历records集合  item == records
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            //将item的数据，拷贝到给我们刚创建的dishDto对象
            BeanUtils.copyProperties(item, ordersDto);
            //获取用户的的id
            Long userId = item.getUserId();
            //根据用户的id ，查询用户的名称
            User user = userService.getById(userId);
            if (user != null) {
                //查询菜品分类的名称
                String name = user.getName();
                ordersDto.setUserName(name);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        page1.setRecords(list);
        return R.success(page1);
    }

    /**
     * 修改用户外卖状态
     */
    @PutMapping
    public R<String> updateOrdersStatus(@RequestParam Orders orders){
        LambdaUpdateWrapper<Orders> ordersLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        ordersLambdaUpdateWrapper.eq(Orders::getId,orders.getId()).set(Orders::getStatus,orders.getStatus());
        ordersService.update(ordersLambdaUpdateWrapper);
        return R.success("订单状态已改变 ");

    }

}

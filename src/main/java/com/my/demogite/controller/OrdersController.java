package com.my.demogite.controller;

import com.my.demogite.common.BaseContext;
import com.my.demogite.common.R;
import com.my.demogite.entity.Orders;
import com.my.demogite.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 提交订单
     */
    @PostMapping("/submit")
    public R<String> sumbit(Orders orders){
        Long currentId = BaseContext.getCurrentId();
        log.info("==== {}",orders.toString());
        return R.success("提交订单成功");
    }
}

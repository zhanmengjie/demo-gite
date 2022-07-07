package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.OrderDetail;
import com.my.demogite.mapper.OrderDetailMapper;
import com.my.demogite.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>implements OrderDetailService {
}

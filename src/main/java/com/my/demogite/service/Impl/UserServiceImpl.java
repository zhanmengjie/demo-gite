package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.User;
import com.my.demogite.mapper.UserMappper;
import com.my.demogite.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMappper, User> implements UserService {
}

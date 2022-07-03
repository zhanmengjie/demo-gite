package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.Employee;
import com.my.demogite.mapper.EmployeeMapper;
import com.my.demogite.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

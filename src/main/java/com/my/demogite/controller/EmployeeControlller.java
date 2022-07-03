package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.demogite.common.R;
import com.my.demogite.entity.Employee;
import com.my.demogite.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeControlller {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //对用户密码进行加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据前端传入的用户名对数据查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //判断查出的用户名是否为空，如果为空登陆失败
        if (emp == null) {
            return R.error("用户名为空，登陆失败");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误，登陆失败!");
        }
        if (emp.getStatus() == 0) {
            return R.error("用户账号以禁用!");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /**
     * 用户退出，清空session中的id
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }
    /**
     * 新增员工信息
     */
    @PostMapping()
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("新增员工信息:{}",employee.toString());
        //设置初始密码使用MD5加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(md5DigestAsHex);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //获取当前用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        //可能会抛出异常
        employeeService.save(employee);
        return R.success("新增员工成功!!!");
    }
    /**
     * 员工信息的分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造条件分页
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        //添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,wrapper);
        //执行查询
        return R.success(pageInfo);
    }
    /**
     * 禁用员工账号
     */
    @PutMapping
    public R<String> updateStatus(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("线程ID为 {}",id);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功!!!");
    }
    /**
     * 操作员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> updateEmployee(@PathVariable Long id){
        Employee byId = employeeService.getById(id);
        if (byId != null) {
            return R.success(byId);
        }
        return R.error("操作失败");

    }
}

package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.my.demogite.Utils.ValidateCodeUtils;
import com.my.demogite.common.R;
import com.my.demogite.entity.User;
import com.my.demogite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){

        log.info("map= {}" ,map);
        String phone = map.get("phone").toString();

        String code = map.get("code").toString();
        Object codeInSession = session.getAttribute(phone);

        if (codeInSession != null && codeInSession.equals(code) ) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断手机号是否为新用户，如果是新用户自动完成注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败!");
    }

    /**
     * 用户退出
     */
    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功!");
    }
    /**
     * 发送手机验证码的请求
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //首先获取手机号
        log.info("生成的验证码: {}",user.getPhone());
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            //生成随机的手机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //调用阿里云短信服务，来完成发送短信
//            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            log.info("code  = {}",code);
            //先将生成的验证码保存一份根据用户输入的  验证码保存到 session进行校验
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("手机短信发送失败");
    }
}

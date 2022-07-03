package com.my.demogite;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com/my/demogite/mapper")
@ServletComponentScan  //扫描过滤器
@EnableTransactionManagement
public class DemoGiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGiteApplication.class, args);
        log.info("项目启动成功  ");
    }

}

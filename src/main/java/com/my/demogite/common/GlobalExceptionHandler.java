package com.my.demogite.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类
 */
@ControllerAdvice(annotations = {
        RestController.class, Controller.class
})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplication entry")) {
            String[] split = ex.getMessage().split("");
            String msg = split[2] + "以存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.info(ex.getMessage());

        return R.error(ex.getMessage());
    }
}

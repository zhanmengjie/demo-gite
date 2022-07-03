package com.my.demogite.common;

/**
 * ThreadLocal的工具封装类 用来获取到保存当前登录用户的id
 */

public class BaseContext {
    private static ThreadLocal<Long> ThreadLocal = new ThreadLocal<Long>();
    public static void setCurrentId(Long id){
        ThreadLocal.set(id);
    }
    public static Long getCurrentId(){
        return ThreadLocal.get();
    }
}

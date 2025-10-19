package com.example.webstarter.Limit;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/19 16:56
 * @注释 ·· 用于限制请求频率的注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
    /**
     * 允许请求的量
     *
     * @return
     */
    int limit();

    /**
     * 指定时间范围，单位秒
     *
     * @return
     */
    int second();

    /**
     * 如果出现了拦截，那么就按照msg文案进行提示
     *
     * @return
     */
    String msg() default "请求过于频繁";
}

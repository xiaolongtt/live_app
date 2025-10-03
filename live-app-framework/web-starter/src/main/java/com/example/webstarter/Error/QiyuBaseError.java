package com.example.webstarter.Error;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 15:18
 * @注释 我们自定义异常的接口规范
 */
public interface QiyuBaseError {
    int getErrorCode();
    String getErrorMsg();
}

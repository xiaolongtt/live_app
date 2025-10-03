package com.example.webstarter.Error;

import java.io.Serial;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 15:19
 * @注释 自定义异常类，实现了自定义异常的接口规范
 */
public class QiyuErrorException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -5253282130382649365L;
    private int errorCode;
    private String errorMsg;

    public QiyuErrorException(int errorCode,String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public QiyuErrorException(QiyuBaseError qiyuBaseError) {
        this.errorCode = qiyuBaseError.getErrorCode();
        this.errorMsg = qiyuBaseError.getErrorMsg();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

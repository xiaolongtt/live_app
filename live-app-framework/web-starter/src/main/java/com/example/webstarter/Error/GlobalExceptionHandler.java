package com.example.webstarter.Error;

import jakarta.servlet.http.HttpServletRequest;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 15:22
 * @注释 自定义全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WebResponseVO errorHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(request.getRequestURI() + ",error is ", e);
        return WebResponseVO.sysError("系统异常");
    }


    /**
     * 当出现异常以后，都会走到这里，输出日志，并且把异常信息返回给前端
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = QiyuErrorException.class)
    @ResponseBody
    public WebResponseVO sysErrorHandler(HttpServletRequest request, QiyuErrorException e) {
        //业务异常，参数传递有误,都会走到这里
        LOGGER.error(request.getRequestURI() + ",error code is {},error msg is {}", e.getErrorCode(), e.getErrorMsg());
        return WebResponseVO.bizError(e.getErrorCode(), e.getErrorMsg());
    }
}

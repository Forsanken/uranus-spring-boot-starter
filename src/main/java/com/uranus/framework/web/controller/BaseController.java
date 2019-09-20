package com.uranus.framework.web.controller;

import com.uranus.framework.web.enums.ErrorCodeEnum;
import com.uranus.framework.web.exception.BaseException;
import com.uranus.framework.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class BaseController {

    @ExceptionHandler
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request, Exception ex) {
        BaseException customException;
        StackTraceElement se = ex.getStackTrace()[0];
        log.error("数据处理出现异常:" + se.getClassName() + ":" + se.getMethodName());

        if (ex instanceof BaseException) {
            customException = (BaseException) ex;
        } else if (ex instanceof RuntimeException) {
            customException = new BaseException(ErrorCodeEnum.RUNTIME_ERROR.getCode(), "数据处理出现异常:" + ex.getMessage());
        } else {
            customException = new BaseException(ErrorCodeEnum.OTHER_ERROR.getCode(), "未知异常:" + ex.getMessage());
        }
        return new Result<>(customException);
    }
}
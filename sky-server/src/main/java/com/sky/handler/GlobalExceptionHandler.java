package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获sql异常
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //获取异常的具体信息
        String message = ex.getMessage();
        //判断是否包含”Duplicate entry”
        if(message.contains("Duplicate entry")){
            //将信息分割成数组
            String[] split = message.split(" ");
            //获得第三个字段即用户名信息
            String username = split[2];
            //构造具体的提示信息，如：zfl + “已存在”
            message =  username + MessageConstant.ALREEADY_EXISTS;
            //返回带有具体提示信息的错误结果
            return Result.error(message);
        }else{
            //如果不是重复条目异常，则返回通用的未知错误提示
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}

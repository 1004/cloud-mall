package com.xky.mall.common.filter;

import com.xky.mall.common.common.CommonResponse;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/20 5:44 下午
 * 所有异常的捕获处理
 */
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        logger.error("Default Exception", e);
        return CommonResponse.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 处理业务异常
     *
     * @param mallException
     * @return
     */
    @ExceptionHandler(MallException.class)
    public Object handleMallException(MallException mallException) {
        logger.error("mall Exception", mallException);
        return CommonResponse.error(mallException.getCode(), mallException.getMsg());
    }

    /**
     * 方法参数异常
     *
     * @param validException
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException validException) {
        logger.error("handleMethodArgumentNotValidException Exception", validException);
        return handleBindingResult(validException.getBindingResult());
    }

    private Object handleBindingResult(BindingResult result) {
        List<String> errors = new ArrayList<>();
        if (result != null && result.getAllErrors() != null) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (int i = 0; i < allErrors.size(); i++) {
                errors.add(allErrors.get(i).getDefaultMessage());
            }
        }
        if (errors.size() > 0) {
            return CommonResponse.error(MallExceptionEnum.SYSTEM_PARAM_EXCEPTION.getCode(), errors.toString());
        } else {
            return CommonResponse.error(MallExceptionEnum.SYSTEM_PARAM_EXCEPTION);
        }
    }
}

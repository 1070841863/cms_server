package com.xuecheng.framework.interceptor;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author study
 * @create 2020-04-03 20:41
 */
//异常捕获类
@ControllerAdvice //控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map，配置异常类型所对应的代码 ImmutableMap是com.google.guava下的类
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> exceptionMaps;
    //定义map的build对象
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder=ImmutableMap.builder();

    static{
        //定义了异常类型所对应的代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

    /*********这是自定义异常捕获**************/
    //只要是这类异常出来，就会被捕获
    @ExceptionHandler(CustomException.class)
    @ResponseBody //将错误消息转json发给客户端
    public ResponseResult customerException(CustomException e){
        //记录日志
        LOGGER.error("catch exception:{}",e.getMessage());
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**************这是不可预知预测捕获**************************/
    //
    @ExceptionHandler(Exception.class)
    @ResponseBody //将错误消息转json发给客户端
    public ResponseResult exception(Exception e){
        //记录日志
        LOGGER.error("catch exception:{}",e.getMessage());
        if(exceptionMaps==null){
            //map构建成功，这个谷歌的map不可更改
            exceptionMaps=builder.build();
        }
        //从exceptionMaps中找异常类型所对应的错误，如果找到了就将错误代码现响应给用户，如果找不到，给用户响应99999异常
        ResultCode resultCode = exceptionMaps.get(e.getClass());
        //说明找到了对应的异常响应
        if(resultCode!=null){
            return new ResponseResult(resultCode);
        }else{
            //返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
}

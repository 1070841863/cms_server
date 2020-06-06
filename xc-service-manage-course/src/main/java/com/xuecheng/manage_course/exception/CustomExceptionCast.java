package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.interceptor.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author study
 * @create 2020-05-03 16:32
 */
@ControllerAdvice
public class CustomExceptionCast extends ExceptionCatch {
    static{
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}

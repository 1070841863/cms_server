package com.xuecheng.framework.interceptor;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author study
 * @create 2020-04-03 20:38
 */
public class ExceptionCast {

    public static void cast(ResultCode code){
        throw  new CustomException(code);
    }
}

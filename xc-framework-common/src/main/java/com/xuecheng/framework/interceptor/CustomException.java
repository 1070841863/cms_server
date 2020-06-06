package com.xuecheng.framework.interceptor;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author 自定义异常类型
 * @create 2020-04-03 20:35
 */
public class CustomException extends RuntimeException  {

     ResultCode resultCode;

     public CustomException(ResultCode code){
         this.resultCode=code;
     }

     public ResultCode getResultCode() {
         return resultCode;
     }
}

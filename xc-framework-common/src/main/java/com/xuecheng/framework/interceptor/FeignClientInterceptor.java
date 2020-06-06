package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.val;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 * @author study
 * @create 2020-05-03 19:14
 */
public class FeignClientInterceptor implements RequestInterceptor {

    //每次feign调用都会调用这个方法
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();
            //取出当前请求的header，找到jwt令牌
            Enumeration<String> headerNames = request.getHeaderNames();
            if(headerNames!=null){
                while(headerNames.hasMoreElements()){
                    String headName = headerNames.nextElement();
                    String headerValue = request.getHeader(headName);
                    //将header向下传递
                    requestTemplate.header(headName, headerValue);
                }
            }

        }
        //取出取出当前header，找到jwt令牌

        //将jwt令牌向下传递
    }
}

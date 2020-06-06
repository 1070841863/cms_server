package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.FilterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 身份校验过滤器
 * @author study
 * @create 2020-04-26 16:33
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private FilterService filterService;

    //设置过滤器类型
    @Override
    public String filterType() {
        /**
         * pre:请求在被录用之前执行
         * routing:在路由请求时调用
         * post:在routing和error过滤器之后调用
         * error:处理请求时发生错误调用
         */

        return "pre";
    }

    //过滤器序号，越小越被优先执行
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回true要执行此此过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        //取cookie中的身份令牌
        String tokenFromCookie = filterService.getTokenFromCookie(request);
        if(StringUtils.isEmpty(tokenFromCookie)){
            //拒绝访问
            access_denied();
            return null;
        }
        //从header取出jwt
        String jwtFromHeader = filterService.getJwtFromHeader(request);
        if(StringUtils.isEmpty(jwtFromHeader)){
            //拒绝访问
            access_denied();
            return null;
        }
        //从redis取出jwt的过期时间
        long expire = filterService.getExpire(tokenFromCookie);
        if(expire<0){
            //拒绝访问
            access_denied();
            return null;
        }
        return null;
    }

    //拒绝访问
    private void access_denied(){
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false);//拒绝访问
        requestContext.setResponseStatusCode(200);//设置响应状态码
        ResponseResult responseResult=new ResponseResult(CommonCode.UNAUTHENTICATED);
        //转为json
        String s = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(s);
        //转成json,设置contextType
        response.setContentType("application/json;charset=utf-8");
    }
}

package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.ext.UserToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author study
 * @create 2020-04-24 22:12
 */
@Service
public class AuthService {


    @Value("${auth.tokenValiditySeconds}")
    private long tokenValiditySeconds;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    //用户认证申请令牌，将令牌储存到redis
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //请求spring security申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if(authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //将令牌储存到redis
        //用户身份令牌
        String access_token = authToken.getAccess_token();
        //储存到redis
        String jsonString = JSON.toJSONString(authToken);
        boolean b = saveToken(access_token, jsonString, tokenValiditySeconds);
        if(!b){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }


    //申请令牌
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        //请求spring security申请令牌
        //从eureka获取地址，因为springsecurity在认证服务中。
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        //此地址就是 http://ip:port
        URI uri = serviceInstance.getUri();
        String authUrl=uri+"/auth/oauth/token";
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization",httpBasic);
        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String,String>> httpEntity=new HttpEntity<>(body,header);
        //设置restTemplate远程调用的时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<Map> map = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        //申请令牌的信息
        Map bodyMap = map.getBody();
        if(bodyMap==null||bodyMap.get("access_token")==null||
                bodyMap.get("refresh_token")==null||
                bodyMap.get("jti")==null){
            //获取spring srcurity返回的错误信息
            String error_description= (String) bodyMap.get("error_description");
            if(StringUtils.isNotEmpty(error_description)){
                if(error_description.indexOf("坏的凭证")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }else if(error_description.indexOf("UserDetailsService returned null")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
            }
            return null;
        }
        AuthToken authToken=new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));//用户身份令牌
        authToken.setJwt_token((String) bodyMap.get("access_token"));//jwt令牌
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));//刷新令牌
        return authToken;
    }

    /**
     *
     * @param access_token 用户身份令牌
     * @param content   内容就是AuthToken对象的内容
     * @param ttl 过期时间
     * @return
     */
    //保存token到redis
    private boolean saveToken(String access_token,String content,long ttl){
        //令牌名称
        String name="user_token:"+access_token;
        //保存到令牌到redis
        stringRedisTemplate.boundValueOps(name).set(content,ttl, TimeUnit.SECONDS);
        //获取过期时间
        Long expire = stringRedisTemplate.getExpire(name);
        return expire>0;

    }

    //从redis查询令牌
    public AuthToken getUserToken(String token){
        //令牌名称
        String name="user_token:"+token;
        //从redis获取令牌
        String s = stringRedisTemplate.opsForValue().get(name);
        //转成对象
        AuthToken authToken = null;
        try {
            authToken = JSON.parseObject(s, AuthToken.class);
            return authToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    //获取httpbasic的串
    private String getHttpBasic(String ClientId,String ClientSecret){
        String s=ClientId+":"+ClientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic "+new String(encode);
    }


    public boolean delToken(String token){
        String name="user_token:"+token;
        stringRedisTemplate.delete(name);
        return true;
    }


}

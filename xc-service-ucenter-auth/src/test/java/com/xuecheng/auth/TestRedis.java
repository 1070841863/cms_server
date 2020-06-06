package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author study
 * @create 2020-04-24 20:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){

        String key="usertoken:39d49e73-2133-4a1c-aec2-c10fda380e6c";
        Map<String,String> map=new HashMap<String,String>();
        map.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU4Nzc1ODMzMSwianRpIjoiMzlkNDllNzMtMjEzMy00YTFjLWFlYzItYzEwZmRhMzgwZTZjIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.fDqpf-DbuWjgyaCnMneNLie0ciAb0nTuagUE8AGRsNDJWVC0l6c5rXHJJ03d-AKpKTqOhZvLQdhz_ieG7jDuwiVHMg9VdBlZS5IHG_TXAB8FeYlmX-hHfgGjHNNw3cnIM2dFIgcOlr_y26u7w9c-mnRMDPNS8sFHj-7chjAoB1LzKiZV9g-G24tySSgSoSYcOHKWEcaOxPsfLzT73s2zZuzFuGdhPx71N4Npig-LqDo5JfUL5U0P1nDED9gdS5_bv1gaCrmtDe6xtWkwQHo67jTUysAXqXTT3Jf_HHNAicO69IXHtM3JkvAqGIQbk8XJhqDOYUmnf9mrVyToHg4jhw");
        map.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiIzOWQ0OWU3My0yMTMzLTRhMWMtYWVjMi1jMTBmZGEzODBlNmMiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU4Nzc1ODMzMSwianRpIjoiMmFiNThmMjQtZTUyZC00NzYzLWEzM2UtNWVmYWRkZGRiOTQ1IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.MBsBABfLBBwnZq-_PLiksfmKLk7xzvqPXI3XBJBbkZCsnWzzT1RghYZ9CM1Fo23dHcO6t38AHPszo8BtK26pYwW9SGPd8uTyGKACcR2J-yGwdHLFWL1TjLgyeojP4NO7lDkWKGZiUbEyJDRPjliyEPBZJ_XQKPjNlVRJA96i7y0JMbQMrPETbFMe9Ypz8LcX59bIkPA4My1EehEzIqjr0Ga-kxLOVjoq0P7NHTmHrN8MX5Y4BqB1qDJm3qb8Xwihy1O2Uem3olqjradoDwi7N_wzjyOEw2qWxsOywdUVE-d4gG-SQdO5bsQ-pMuEUtOH00A4txClDAL5moFmQ7iGow");
        String string = JSON.toJSONString(map);
        //储存数据
        stringRedisTemplate.boundValueOps(key).set(string,30, TimeUnit.SECONDS);
        //获取数据
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);


    }
}

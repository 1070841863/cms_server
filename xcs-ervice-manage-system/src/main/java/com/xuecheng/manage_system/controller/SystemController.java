package com.xuecheng.manage_system.controller;

import com.alibaba.fastjson.JSON;
import com.xuecheng.api.system.SystemDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_system.service.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author study
 * @create 2020-04-08 15:58
 */
@RestController
@RequestMapping("sys")
public class SystemController implements SystemDicthinaryControllerApi {
    @Autowired
    private SysDicService sysDicService;

    @GetMapping("/dictionary/get/{type}")
    public SysDictionary getInfoByType(@PathVariable("type") String type) {
        return sysDicService.getInfoByType(type);
    }


    @PostMapping("/login")
    public Map login(@RequestBody Userinfo userinfo){
        System.out.println(userinfo);
        Map<String,String> map=new HashMap<>();
        map.put("msg","ok");
        map.put("code","200");
        return map;
    }

    @GetMapping("/info")
    public String info(String callback){
        Map<String,String> map=new HashMap<>();
        map.put("x1","123");
        map.put("x2","345");
        String s="";
        if(callback!=null){
            s=callback+"("+JSON.toJSONString(map)+")";
        }
        else{
            s=JSON.toJSONString(map);
        }
        return s;
    }
}

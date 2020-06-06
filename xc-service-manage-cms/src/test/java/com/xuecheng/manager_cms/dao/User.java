package com.xuecheng.manager_cms.dao;

/**
 * @author study
 * @create 2020-04-16 10:27
 */
public class User {

    private String url;

    public String say(String name){
        System.out.println(name);
        System.out.println("url"+url);
        return name;
    }

}

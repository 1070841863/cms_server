package com.xuecheng.manager_cms.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author study
 * @create 2020-04-16 10:27
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("com.xuecheng.manager_cms.dao.User");
        Method say = aClass.getDeclaredMethod("say", String.class);
        Field field = aClass.getDeclaredField("url");
        field.setAccessible(true);
        field.set(aClass.newInstance(),"123");
        String res = (String) say.invoke(aClass.newInstance(), "hello");
        User o = (User) aClass.newInstance();
    }
}

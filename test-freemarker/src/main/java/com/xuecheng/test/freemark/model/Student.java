package com.xuecheng.test.freemark.model;


import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author study
 * @create 2020-04-04 14:24
 */
@Data
@ToString
@AllArgsConstructor
public class Student {

    private String name;
    private Integer age;
    private Date birthday;
    private Float money;
    private List<Student> friends;
    private Student bestFriends;
}

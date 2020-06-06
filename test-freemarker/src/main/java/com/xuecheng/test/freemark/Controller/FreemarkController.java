package com.xuecheng.test.freemark.Controller;

import com.xuecheng.test.freemark.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author study
 * @create 2020-04-04 14:39
 */
@Controller
@RequestMapping("/freemarker")
public class FreemarkController {

    @RequestMapping("/test11")
    public String test1(Map<String,Object> map){
        List<Student> ar=new ArrayList<Student>();
        List<Student> s1=new ArrayList<Student>();
        s1.add(new Student("张三朋友1",22,new Date(),200.0f,null,null));
        Student student=new Student("张三",11,new Date(),100.0f,s1,null);
        ar.add(student);
        s1.add(new Student("李四朋友1",22,new Date(),200.0f,null,null));
        Student student2=new Student("李四",33,new Date(),333f,null,null);
        ar.add(student2);
        map.put("ar",ar);


        HashMap<String,Student> stumap=new HashMap<>();
        stumap.put("s1",student);
        stumap.put("s2",student2);

        map.put("stumap",stumap);

        map.put("name","学习！！！！");
        return "test1";
    }

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/banner")
    public String testLunbo(Map<String,Object> map){
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map map1 = entity.getBody();
        map.putAll(map1);
        return "index_banner";
    }

    @RequestMapping("/coursedesc")
    public String coursedesc(Map<String,Object> map){
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://localhost:31200/course/courseview/402885816240d276016240f7e5000002", Map.class);
        Map map1 = entity.getBody();
        map.putAll(map1);
        return "course";
    }
}

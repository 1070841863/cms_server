package com.xuecheng.test.freemark;

import com.xuecheng.test.freemark.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.*;

/**
 * @author study
 * @create 2020-04-04 16:32
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {

    //测试静态化，基于ftl模板文件生成html文件
    @Test
    public void test() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //定义模板
        //得到classpath路径
        String path = this.getClass().getResource("/").getPath();
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
        //获取模板文件内容
        Template template = configuration.getTemplate("test1.ftl");
        //定义数据模型
        Map map = this.getmap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        File f=new File("d:\\test\\test1.html");
        FileOutputStream outputStream=new FileOutputStream(f);
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    //获取数据模型
    public Map getmap(){
        Map<String,Object> map=new HashMap<>();
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
        return map;
    }


    @Test
    public void testByString() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //定义模板
        String mystring="<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf‐8\">\n" +
                "    <title>Hello World!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hello ${name}!使用串\n" +
                "</body>\n" +
                "</html>";
        //使用一个模板加载器变为模板
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        //定义模板名称和模板里的值
        stringTemplateLoader.putTemplate("template_test",mystring);
        //在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        Template template = configuration.getTemplate("template_test", "utf-8");
        //定义数据内容
        //定义数据模型
        Map map = this.getmap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(content);
        File f=new File("d:\\test\\test2.html");
        FileOutputStream outputStream=new FileOutputStream(f);
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }
}

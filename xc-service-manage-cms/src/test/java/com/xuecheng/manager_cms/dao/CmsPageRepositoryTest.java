package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author study
 * @create 2020-03-31 21:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    //分页查询
    @Test
    public void testFindPage() {
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //添加
    @Test
    public void testInsert() {
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    //删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5b17a2c511fe5e0c409e5eb3");
    }

    //修改
    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5b17a34211fe5e2ee8c116c9");
        //关于Optional： Optional是jdk1.8引入的类型，Optional是一个容器对象，它包括了我们需要的对象，
        // 使用isPresent方法判断所包 含对象是否为空，isPresent方法返回false则表示Optional包含对象为空，
        // 否则可以使用get()取出对象进行操作。
        // Optional的优点是：
        // 1、提醒你非空判断。
        // 2、将对象非空检测标准化。
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRepository.save(cmsPage);
        }
    }

    @Test
    public void testFindAllByExample(){
        int page=0;
        int size=10;
        Pageable pageable=PageRequest.of(page,size);
        //条件值对象
        CmsPage cmsPage=new CmsPage();
        //想要查询的条件，就放入cmsPage对象中。可以进行精确匹配
        //要查询5a751fab6abb5044e0d19ea1的页面
//        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
//        cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");
        //如果要使用模糊匹配,那就需要添加ExampleMatcher
        cmsPage.setPageAliase("轮播");
        //条件匹配器
        ExampleMatcher exampleMatcher=ExampleMatcher.matching();
        exampleMatcher=exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //定义Example
        Example<CmsPage> cmsPageExample=Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(cmsPageExample, pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
        CmsConfig cmsConfig = restTemplate.getForObject("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", CmsConfig.class);
        System.out.println(cmsConfig);
    }
}
